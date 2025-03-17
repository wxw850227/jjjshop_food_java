package net.jjjshop.front.service.user.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.common.enums.BalanceLogEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.mapper.user.UserMapper;
import net.jjjshop.common.settings.vo.PointsVo;
import net.jjjshop.common.settings.vo.SignVo;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.util.UserUtils;
import net.jjjshop.common.util.wx.AppWxUtils;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.config.properties.JwtProperties;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import net.jjjshop.framework.shiro.cache.UserLoginRedisService;
import net.jjjshop.framework.shiro.jwt.JwtToken;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.framework.shiro.util.JwtUtil;
import net.jjjshop.framework.shiro.util.SaltUtil;
import net.jjjshop.framework.shiro.vo.LoginUserVo;
import net.jjjshop.framework.util.LoginUtil;
import net.jjjshop.framework.util.PasswordUtil;
import net.jjjshop.front.param.AppAppleParam;
import net.jjjshop.front.param.AppWxParam;
import net.jjjshop.front.param.user.PhoneRegisterParam;
import net.jjjshop.front.service.user.*;
import net.jjjshop.front.vo.user.LoginUserTokenVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-01
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

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
    private UserLoginRedisService userLoginRedisService;
    @Lazy
    @Autowired
    private WxMaService wxMaService;
    @Lazy
    @Autowired
    private AppWxUtils appWxUtils;
    @Lazy
    @Autowired
    private SettingUtils settingUtils;
    @Lazy
    @Autowired
    private UserUtils userUtils;
    @Lazy
    @Autowired
    private UserBalanceLogService userBalanceLogService;

    /**
     * 手机号密码登录
     *
     * @param mobile
     * @param password
     * @return
     */
    @Override
    public LoginUserTokenVo login(String mobile, String password) {
        List<User> userList = this.list(new LambdaQueryWrapper<User>()
                .eq(User::getMobile, mobile).ne(User::getPassword, ""));
        if (userList.size() == 0) {
            throw new AuthenticationException("用户名不存在");
        }
        User user = userList.get(0);
        String encryptPassword = PasswordUtil.encrypt(password, user.getSalt());
        if (!encryptPassword.equals(user.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }
        return this.getLoginUserTokenVo(user);
    }

    /**
     * 获取登陆用户token
     *
     * @param user
     * @return
     */
    private LoginUserTokenVo getLoginUserTokenVo(User user) {
        // 将系统用户对象转换成登录用户对象
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
        // 获取数据库中保存的盐值
        String newSalt = SaltUtil.getSalt(user.getSalt(), jwtProperties);

        // 生成token字符串并返回
        Long expireSecond = jwtProperties.getExpireSecond();
        String token = JwtUtil.generateToken("" + user.getUserId(), newSalt, Duration.ofSeconds(expireSecond));
        log.debug("token:{}", token);

        // 创建AuthenticationToken
        JwtToken jwtToken = JwtToken.build(token, "" + user.getUserId(), newSalt, expireSecond);

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
        userLoginRedisService.cacheLoginInfo(jwtToken, loginUserVo);
        log.debug("登录成功,userId:{}", "" + user.getUserId());

        // 缓存登录信息到redis
        String tokenSha256 = DigestUtils.sha256Hex(token);
        redisTemplate.opsForValue().set(tokenSha256, loginUserVo, 1, TimeUnit.DAYS);

        // 返回token和登录用户信息对象
        LoginUserTokenVo loginUserTokenVo = new LoginUserTokenVo();
        loginUserTokenVo.setToken(token);
        loginUserTokenVo.setLoginUserVo(loginUserVo);
        return loginUserTokenVo;
    }

    /**
     * 微信小程序登录
     *
     * @param appWxParam
     * @return
     */
    @Override
    public LoginUserTokenVo loginWx(AppWxParam appWxParam) {
        WxMaJscode2SessionResult result;
        try {
            result = wxMaService.switchoverTo(appWxUtils.getConfig(wxMaService, null)).getUserService().getSessionInfo(appWxParam.getCode());
        } catch (WxErrorException e) {
            log.info("微信小程序登录异常：{}", e.getMessage());
            throw new BusinessException("微信小程序登录异常：" + e.getError().getErrorMsg());
        }
        // 查找用户是否存在，不存在则先注册
        User user = null;
        //先通过unionId查找
        if (StringUtils.isNotBlank(result.getUnionid())) {
            user = getUserByUnionId(result.getUnionid());
        }
        if (user == null) {
            // 通过你openId查找
            user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, result.getOpenid()));
        }
        if (user != null) {
            return this.getLoginUserTokenVo(user);
        } else {
            // 注册后返回
            String userInfoStr = StringEscapeUtils.unescapeHtml4(appWxParam.getUserInfo());
            JSONObject userInfo = JSONObject.parseObject(userInfoStr);
            User bean = new User();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(result.getUnionid())) {
                bean.setUnionId(result.getUnionid());
            }
            bean.setOpenId(result.getOpenid());
            bean.setRegSource("wx");
            if(userInfo != null){
                bean.setAvatarUrl(userInfo.getString("avatarUrl"));
                if(userInfo.getString("nickName").equals("微信用户")){
                    //截取后6位
                    bean.setNickName(result.getOpenid().substring(result.getOpenid().length() - 6));
                }else{
                    bean.setNickName(userInfo.getString("nickName"));
                }
            }else {
                this.save(bean);
                //修改用户默认昵称和头像
                this.updateDefaultUser(bean);
            }
            this.saveOrUpdate(bean);
            this.afterReg(bean.getUserId(), appWxParam.getRefereeId(), appWxParam.getInvitationId());
            return this.getLoginUserTokenVo(bean);
        }
    }

    /**
     * 微信公众号登录
     * @param wxMpUser
     * @return
     */
    @Override
    public LoginUserTokenVo loginMp(WxOAuth2UserInfo wxMpUser, Integer refereeId, Integer invitationId){
        User user = null;
        // 查找用户是否存在，不存在则先注册
        //先通过unionId查找
        if(StringUtils.isNotBlank(wxMpUser.getUnionId())){
            user = getUserByUnionId(wxMpUser.getUnionId());
        }
        if (user == null) {
            // 通过你openId查找
            user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getMpopenId, wxMpUser.getOpenid()));
        }
        if (user != null) {
            return this.getLoginUserTokenVo(user);
        } else {
            // 注册后返回
            User bean = new User();
            if (StringUtils.isNotBlank(wxMpUser.getUnionId())) {
                bean.setUnionId(wxMpUser.getUnionId());
            }
            bean.setMpopenId(wxMpUser.getOpenid());
            bean.setNickName(wxMpUser.getNickname());
            bean.setAvatarUrl(wxMpUser.getHeadImgUrl());
            bean.setRegSource("mp");
            this.save(bean);
            this.afterReg(bean.getUserId(), refereeId, invitationId);
            return this.getLoginUserTokenVo(bean);
        }
    }

    /**
     * 微信开放平台登录
     * @param userInfo
     * @return
     */
    @Override
    public LoginUserTokenVo loginOpen(WxOAuth2UserInfo userInfo){
        User user = null;
        // 查找用户是否存在，不存在则先注册
        //先通过unionId查找
        if(StringUtils.isNotBlank(userInfo.getUnionId())){
            user = getUserByUnionId(userInfo.getUnionId());
        }
        if (user == null) {
            // 通过你openId查找
            user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getAppopenId, userInfo.getOpenid()));
        }
        if (user != null) {
            return this.getLoginUserTokenVo(user);
        } else {
            // 注册后返回
            User bean = new User();
            if (StringUtils.isNotBlank(userInfo.getUnionId())) {
                bean.setUnionId(userInfo.getUnionId());
            }
            bean.setAppopenId(userInfo.getOpenid());
            bean.setNickName(userInfo.getNickname());
            bean.setAvatarUrl(userInfo.getHeadImgUrl());
            bean.setRegSource("app");
            this.save(bean);
            return this.getLoginUserTokenVo(bean);
        }
    }

    /**
     * ios一键登录
     * @param appAppleParam
     * @return
     */
    @Override
    public LoginUserTokenVo loginApple(AppAppleParam appAppleParam){
        // 查找用户是否存在，不存在则先注册
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getAppUser, appAppleParam.getOpenId()));
        if (user != null) {
            return this.getLoginUserTokenVo(user);
        } else {
            // 注册后返回
            User bean = new User();
            bean.setAppUser(appAppleParam.getOpenId());
            bean.setNickName(appAppleParam.getNickName());
            bean.setRegSource("apple");
            this.save(bean);
            //修改用户默认昵称和头像
            this.updateDefaultUser(bean);
            return this.getLoginUserTokenVo(bean);
        }
    }

    /**
     * 手机号验证码登录
     * @param mobile
     * @param code
     * @return
     */
    @Override
    public LoginUserTokenVo loginSms(String mobile, String code){
        List<User> userList = this.list(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile)
                .orderByAsc(User::getCreateTime));
        if(userList.size() == 0){
            throw new BusinessException("手机号不存在");
        }
        User user = userList.get(0);
        if (user.getIsDelete() == 1) {
            throw new BusinessException("手机号被禁止或删除，请联系客服");
        }
        return this.getLoginUserTokenVo(user);
    }

    /**
     * h5手机号注册
     */
    @Override
    public LoginUserTokenVo phoneRegister(PhoneRegisterParam phoneRegisterParam) {
        int count = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getIsDelete, 0)
                .eq(User::getMobile, phoneRegisterParam.getMobile()));
        if (!(count > 0)) {
            User user = new User();
            user.setMobile(phoneRegisterParam.getMobile());
            user.setRegSource(phoneRegisterParam.getRegSource() != null ? phoneRegisterParam.getRegSource() : "h5");
            // 盐值
            String salt = SaltUtil.generateSalt();
            // 密码加密
            user.setSalt(salt);
            user.setPassword(PasswordUtil.encrypt(phoneRegisterParam.getPassword(), salt));
            //user.setNickName("用户:"+phoneRegisterParam.getMobile());
            this.save(user);
            //修改用户默认昵称和头像
            this.updateDefaultUser(user);
            this.afterReg(user.getUserId(), phoneRegisterParam.getRefereeId(), phoneRegisterParam.getInvitationId());
            return this.getLoginUserTokenVo(user);
        } else {
            throw new BusinessException("手机号已存在");
        }
    }

    //修改用户默认昵称和头像
    private User updateDefaultUser(User user) {
        JSONObject setting = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = setting.toJavaObject(StoreVo.class);
        user.setNickName(storeVo.getUserName()+user.getUserId());
        user.setAvatarUrl(storeVo.getAvatarUrl());
        this.updateById(user);
        return user;
    }

    /**
     * 微信小程序用户登录
     */
    @Override
    public LoginUserTokenVo userLogin(AppWxParam appWxParam) {
        WxMaJscode2SessionResult result = null;
        try {
            result = wxMaService.switchoverTo(appWxUtils.getConfig(wxMaService, null)).getUserService().getSessionInfo(appWxParam.getCode());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return null;
        }
        // 查找用户是否存在，不存在则先注册
        User user = null;
        //先通过unionId查找
        if (org.apache.commons.lang3.StringUtils.isNotBlank(result.getUnionid())) {
            user = getUserByUnionId(result.getUnionid());
        }
        if (user == null) {
            // 通过你openId查找
            user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, result.getOpenid()));
        }
        if (user != null) {
            return this.getLoginUserTokenVo(user);
        } else {
            throw new BusinessException("用户不存在,请重新登录");
        }
    }

    /**
     * 签到更新用户积分
     */
    @Override
    public Integer setPoints(Integer userId, Integer days, SignVo signVo, String signDate){
        Integer rank = signVo.getEverSign();
        if(signVo.getIsIncrease()){
            if(days >= signVo.getNoIncrease()){
                days = signVo.getNoIncrease() -1;
            }
            rank = (days-1)*signVo.getIncreaseReward()+rank;
        }
        //是否奖励
        if(CollectionUtils.isNotEmpty(signVo.getRewardData())){
            List<Integer> arr = signVo.getRewardData().stream().map(e -> {
                return e.getDay();
            }).collect(Collectors.toList());
            if(arr.contains(days)){
                int key = arr.indexOf(days);
                SignVo.reward reward = signVo.getRewardData().get(key);
                if(reward.getIsPoint()){
                    rank = reward.getPoint()+rank;
                }
            }
        }
        return rank;
    }

    /**
     * 注册之后的操作
     */
    public Boolean afterReg(Integer userId, Integer refereeId,Integer invitationId) {
        // 记录推荐人关系 更新用户邀请数量 邀请好友送好礼
        if (refereeId != null && refereeId > 0 && this.getById(refereeId) != null) {
            User user = new User();
            user.setUserId(userId);
            user.setRefereeId(refereeId);
            this.setIncInvite(refereeId);
            this.updateById(user);
        }
        JSONObject setting = settingUtils.getSetting(SettingEnum.POINTS.getKey(), null);
        PointsVo pointsVo = setting.toJavaObject(PointsVo.class);
        return true;
    }

    /**
     * 累计邀请数
     */
    public Boolean setIncInvite(Integer userId) {
        User user = this.getById(userId);
        user.setTotalInvite(user.getTotalInvite() + 1);
        return this.updateById(user);
    }


    /**
     * 通过unionid查找用户
     *
     * @param unionId
     * @return
     */
    private User getUserByUnionId(String unionId) {
        if (StringUtils.isNotBlank(unionId)) {
            return this.getOne(new LambdaQueryWrapper<User>().eq(User::getUnionId, unionId));
        }
        return null;
    }

    /**
     * 修改密码
     * @param mobile
     * @param code
     * @param password
     * @return
     */
    @Override
    public Boolean renew(String mobile, String code, String password) {
        List<User> userList = this.list(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile)
                .orderByAsc(User::getCreateTime));
        if(userList.size() == 0){
            throw new BusinessException("手机号不存在");
        }
        User user = userList.get(0);
        if (user.getIsDelete() == 1) {
            throw new BusinessException("手机号被禁止或删除，请联系客服");
        }
        User updateUser = new User();
        updateUser.setUserId(user.getUserId());
        updateUser.setPassword(DigestUtils.md5Hex(password));
        return true;
    }

    /**
     * 退出
     *
     * @param request
     * @throws Exception
     */
    @Override
    public void logout(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        // 获取token
        String token = JwtTokenUtil.getToken(request, "");
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
        redisTemplate.delete(String.format(CommonRedisKey.USER_LOGIN_TOKEN, tokenMd5));
        // 2. delete username
        redisTemplate.delete(String.format(CommonRedisKey.USER_LOGIN_USER, username));
        // 3. delete salt
        redisTemplate.delete(String.format(CommonRedisKey.USER_LOGIN_SALT, username));
        // 4. delete user token
        redisTemplate.delete(String.format(CommonRedisKey.USER_LOGIN_USER_TOKEN, username, tokenMd5));
    }

    /**
     * 修改用户信息
     *
     * @param userId
     * @param updateType
     * @param updateValue
     * @return
     */
    @Override
    public Boolean updateInfo(Integer userId, String updateType, String updateValue) {
        User user = new User();
        user.setUserId(userId);
        if ("nickName".equals(updateType)) {
            user.setNickName(updateValue);
        } else if ("avatarUrl".equals(updateType)) {
            user.setAvatarUrl(updateValue);
        } else {
            return false;
        }
        return this.updateById(user);
    }

    @Override
    public boolean updateUser(String avatarUrl, String nickName) {
        User user = new User();
        user.setUserId(LoginUtil.getUserId());
        user.setAvatarUrl(avatarUrl);
        user.setNickName(nickName);
        this.updateById(user);
        return true;
    }

    /**
     * 获取sessionKey
     *
     * @param code
     * @return
     */
    @Override
    public String getSessionKey(String code) {
        try {
            WxMaJscode2SessionResult sessionResult = wxMaService.switchoverTo(appWxUtils.getConfig(wxMaService, null)).getUserService().getSessionInfo(code);
            return sessionResult.getSessionKey();
        } catch (Exception e) {
            log.info("getSessionKey失败{}", e.getMessage());
            return "";
        }
    }

    /**
     * 绑定手机号
     *
     * @param encryptedData
     * @param iv
     * @return
     */
    @Override
    public LoginUserTokenVo bindMobileByWx(Integer userId, String code, String encryptedData, String iv) {
        User user = this.getById(userId);
        if(user == null){
            throw new BusinessException("授权失败，请重新授权");
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(user.getMobile())){
            return this.getLoginUserTokenVo(user);
        }
        WxMaPhoneNumberInfo result = null;
        try {
            WxMaJscode2SessionResult sessionResult = wxMaService.switchoverTo(appWxUtils.getConfig(wxMaService, null)).getUserService().getSessionInfo(code);
            String sessionKey = sessionResult.getSessionKey();
            result = wxMaService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
        } catch (Exception e) {
            log.info("手机号解密失败{}", e.getMessage());
            return null;
        }
        String mobile = result.getPhoneNumber();
        User updateUser = new User();
        updateUser.setUserId(userId);
        updateUser.setMobile(mobile);
        this.updateById(updateUser);
        return this.getLoginUserTokenVo(user);
    }

    //小程序或公众号绑定手机号
    @Override
    public boolean bindMobile(String mobile, String code) {
        //判断是否已经注册
        Integer count = this.count(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile)
                .eq(User::getIsDelete, 0)
        );
        if (count > 0) {
            throw new BusinessException("手机号码已存在");
        }
        User updateUser = new User();
        updateUser.setUserId( LoginUtil.getUserId());
        updateUser.setMobile(mobile);
        return this.updateById(updateUser);
    }

}
