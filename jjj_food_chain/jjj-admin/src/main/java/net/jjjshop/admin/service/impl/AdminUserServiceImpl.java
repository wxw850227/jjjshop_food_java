

package net.jjjshop.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.service.AdminUserService;
import net.jjjshop.admin.vo.LoginAdminUserTokenVo;
import net.jjjshop.common.entity.admin.AdminUser;
import net.jjjshop.common.mapper.admin.AdminUserMapper;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.config.properties.JwtProperties;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.shiro.cache.AdminLoginRedisService;
import net.jjjshop.framework.shiro.jwt.JwtToken;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.framework.shiro.util.JwtUtil;
import net.jjjshop.framework.shiro.util.SaltUtil;
import net.jjjshop.framework.shiro.vo.LoginAdminUserVo;
import net.jjjshop.framework.util.PasswordUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


/**
 * 系统用户 服务实现类
 */
@Slf4j
@Service
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Lazy
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;

    @Lazy
    @Autowired
    private JwtProperties jwtProperties;

    @Lazy
    @Autowired
    private RedisTemplate redisTemplate;

    @Lazy
    @Autowired
    private AdminLoginRedisService adminLoginRedisService;

    public LoginAdminUserTokenVo login(String username, String password){
        AdminUser adminUser = adminUserMapper.selectOne(new LambdaQueryWrapper<AdminUser>()
                .eq(AdminUser::getUserName, username));

        if(adminUser == null){
            throw new BusinessException("用户名或密码错误");
        }
        String encryptPassword = PasswordUtil.encrypt(password, adminUser.getSalt());
        if (!encryptPassword.equals(adminUser.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }
        // 将系统用户对象转换成登录用户对象
        LoginAdminUserVo loginAdminUserVo = new LoginAdminUserVo();
        BeanUtils.copyProperties(adminUser, loginAdminUserVo);
        // 获取数据库中保存的盐值
        String newSalt = SaltUtil.getSalt(adminUser.getSalt(), jwtProperties);

        // 生成token字符串并返回
        Long expireSecond = jwtProperties.getExpireSecond();
        String token = JwtUtil.generateToken(username, newSalt, Duration.ofSeconds(expireSecond));
        log.debug("token:{}", token);

        // 创建AuthenticationToken
        JwtToken jwtToken = JwtToken.build(token, username, newSalt, expireSecond);

        boolean enableShiro = springBootJjjProperties.getShiro().isEnable();
        if (enableShiro) {
            // 从SecurityUtils里边创建一个 subject
            Subject subject = SecurityUtils.getSubject();
            // 执行认证登录
            subject.login(jwtToken);
        } else {
            log.warn("未启用Shiro");
        }

        // 缓存登录信息到Redis
        adminLoginRedisService.cacheLoginInfo(jwtToken, loginAdminUserVo);
        log.debug("登录成功,username:{}", username);

        // 缓存登录信息到redis
        String tokenSha256 = DigestUtils.sha256Hex(token);
        redisTemplate.opsForValue().set(tokenSha256, loginAdminUserVo, 1, TimeUnit.DAYS);

        // 返回token和登录用户信息对象
        LoginAdminUserTokenVo loginAdminUserTokenVo = new LoginAdminUserTokenVo();
        loginAdminUserTokenVo.setToken(token);
        loginAdminUserTokenVo.setLoginAdminUserVo(loginAdminUserVo);
        return loginAdminUserTokenVo;
    }

    /**
     * 修改密码
     * @param password
     * @return
     */
    public Boolean renew(String password,String checkPass){
        if(!password.equals(checkPass)){
            throw new BusinessException("两次密码不相同");
        }
        AdminUser adminUser = adminUserMapper.selectOne(new LambdaQueryWrapper<AdminUser>());

        String encryptPassword = PasswordUtil.encrypt(password, adminUser.getSalt());
        AdminUser updateBean = new AdminUser();
        updateBean.setAdminUserId(adminUser.getAdminUserId());
        updateBean.setPassword(encryptPassword);
        return this.updateById(updateBean);
    }

    @Override
    public void logout(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        // 获取token
        String token = JwtTokenUtil.getToken(request, "admin");
        String username = JwtUtil.getUsername(token);
        // 删除Redis缓存信息
        deleteLoginInfo(token, username);
        log.info("登出成功,username:{},token:{}", username, token);
    }

    private void deleteLoginInfo(String token, String username) {
        if (token == null) {
            throw new IllegalArgumentException("token不能为空");
        }
        if (username == null) {
            throw new IllegalArgumentException("username不能为空");
        }
        String tokenMd5 = DigestUtils.md5Hex(token);
        // 1. delete tokenMd5
        redisTemplate.delete(CommonRedisKey.getRedisKey(CommonRedisKey.ADMIN_LOGIN_TOKEN, tokenMd5));
        // 2. delete username
        redisTemplate.delete(CommonRedisKey.getRedisKey(CommonRedisKey.ADMIN_LOGIN_USER, username));
        // 3. delete salt
        redisTemplate.delete(CommonRedisKey.getRedisKey(CommonRedisKey.ADMIN_LOGIN_SALT, username));
        // 4. delete user token
        redisTemplate.delete(CommonRedisKey.getRedisKey(CommonRedisKey.ADMIN_LOGIN_USER_TOKEN, username, tokenMd5));
    }
}
