

package net.jjjshop.framework.shiro.jwt;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.shiro.cache.AdminLoginRedisService;
import net.jjjshop.framework.shiro.cache.ShopLoginRedisService;
import net.jjjshop.framework.shiro.vo.LoginShopUserRedisVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Shiro 授权认证
 **/
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    private AdminLoginRedisService adminLoginRedisService;

    private ShopLoginRedisService shopLoginRedisService;

    @Autowired
    private HttpServletRequest request;

    public JwtRealm(AdminLoginRedisService adminLoginRedisService, ShopLoginRedisService shopLoginRedisService) {
        this.adminLoginRedisService = adminLoginRedisService;
        this.shopLoginRedisService = shopLoginRedisService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JwtToken;
    }

    /**
     * 授权认证,设置角色/权限信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.debug("doGetAuthorizationInfo principalCollection..."+request.getRequestURI());
        // 设置角色/权限信息
        JwtToken jwtToken = (JwtToken) principalCollection.getPrimaryPrincipal();
        // 获取username
        String username = jwtToken.getUsername();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if(request.getRequestURI().startsWith("/api/shop/")){
            // 获取登录用户角色权限信息
            LoginShopUserRedisVo loginShopUserRedisVo = shopLoginRedisService.getLoginShopUserRedisVo(username);
            // 设置权限
            authorizationInfo.setStringPermissions(loginShopUserRedisVo.getPermissions());
        }
        return authorizationInfo;
    }

    /**
     * 登录认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.debug("doGetAuthenticationInfo authenticationToken...");
        // 校验token
        JwtToken jwtToken = (JwtToken) authenticationToken;
        if (jwtToken == null) {
            throw new AuthenticationException("jwtToken不能为空");
        }
        String salt = jwtToken.getSalt();
        if (StringUtils.isBlank(salt)) {
            throw new AuthenticationException("salt不能为空");
        }
        return new SimpleAuthenticationInfo(
                jwtToken,
                salt,
                getName()
        );

    }

}
