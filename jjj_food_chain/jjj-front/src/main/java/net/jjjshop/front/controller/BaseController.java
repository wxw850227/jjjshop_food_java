

package net.jjjshop.front.controller;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.api.ApiCode;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.shiro.vo.LoginUserRedisVo;
import net.jjjshop.framework.util.LoginUtil;
import net.jjjshop.front.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;

@Slf4j
public class BaseController {
    @Value("${spring.profiles.active}")
    private String env;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected UserService userService;
    /**
     * 获取用户信息
     * @param isForce 是否强制
     */
    public User getUser(boolean isForce){
        LoginUserRedisVo vo = null;
        try{
            vo = LoginUtil.getLoginUserRedisVo();
        }catch (Exception e){
            log.info("获取登录用户异常：", e);
        }
        if(vo == null){
            if(isForce){
                throw new BusinessException(ApiCode.NOT_LOGIN);
            }
            return null;
        }
        User user = userService.getById(vo.getUserId());
        if(user == null && isForce){
            throw new BusinessException(-1, "未找到用户信息");
        }
        // 用户存在
        if(user != null){
            if(user.getIsDelete() == 1){
                throw new BusinessException(-2, "账号已禁用");
            }
        }
        return user;
    }
}
