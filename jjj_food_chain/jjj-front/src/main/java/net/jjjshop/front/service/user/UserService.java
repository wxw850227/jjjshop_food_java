package net.jjjshop.front.service.user;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.settings.vo.SignVo;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.param.AppAppleParam;
import net.jjjshop.front.param.AppWxParam;
import net.jjjshop.front.param.user.PhoneRegisterParam;
import net.jjjshop.front.vo.user.LoginUserTokenVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户记录表 服务类
 * @author jjjshop
 * @since 2022-07-01
 */
public interface UserService extends BaseService<User> {

    /**
     * 手机号密码登录
     * @param mobile
     * @param password
     * @return
     */
    LoginUserTokenVo login(String mobile, String password);

    /**
     * 微信小程序登录
     * @param appWxParam
     * @return
     */
    LoginUserTokenVo loginWx(AppWxParam appWxParam);

    /**
     * 微信公众号登录
     * @param wxMpUser
     * @return
     */
    LoginUserTokenVo loginMp(WxOAuth2UserInfo wxMpUser, Integer refereeId, Integer invitationId);

    /**
     * 微信开放平台登录
     * @param userInfo
     * @return
     */
    LoginUserTokenVo loginOpen(WxOAuth2UserInfo userInfo);

    /**
     * ios一键登录
     * @param appAppleParam
     * @return
     */
    LoginUserTokenVo loginApple(AppAppleParam appAppleParam);

    /**
     * 手机号验证码登录
     * @param mobile
     * @param code
     * @return
     */
    LoginUserTokenVo loginSms(String mobile, String code);
    /**
     * 修改密码
     * @param password
     * @return
     */
    Boolean renew(String mobile, String code, String password);

    /**
     * 退出
     * @param request
     * @throws Exception
     */
    void logout(HttpServletRequest request) throws Exception;

    /**
     * 修改用户信息
     * @param userId
     * @param updateType
     * @param updateValue
     * @return
     */
    Boolean updateInfo(Integer userId, String updateType, String updateValue);

    /**
     * 绑定手机号
     * @param encryptedData
     * @param iv
     * @return
     */
    LoginUserTokenVo bindMobileByWx(Integer userId, String code, String encryptedData, String iv);

    /**
     * 获取sessionKey
     * @param code
     * @return
     */
    String getSessionKey(String code);

    /**
     *手机号注册
     */
    LoginUserTokenVo phoneRegister(PhoneRegisterParam phoneRegisterParam);


    /**
     * 签到更新用户积分
     */
    Integer setPoints(Integer userId, Integer days, SignVo signVo, String signDate);

    //h5绑定手机号
    boolean bindMobile(String mobile, String code);

    boolean updateUser(String avatarUrl, String nickName);

    LoginUserTokenVo userLogin(AppWxParam appWxParam);
}
