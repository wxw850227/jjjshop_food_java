package net.jjjshop.common.util.agora;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RtmTokenBuilder {

    /**
     * Build the RTM token.
     *
     * @param appId:          The App ID issued to you by Agora. Apply for a new App ID from
     *                        Agora Dashboard if it is missing from your kit. See Get an App ID.
     * @param appCertificate: Certificate of the application that you registered in
     *                        the Agora Dashboard. See Get an App Certificate.
     * @param userId:         The user's account, max length is 64 Bytes.
     * @param expire:         represented by the number of seconds elapsed since now. If, for example, you want to access the
     *                        Agora Service within 10 minutes after the token is generated, set expireTimestamp as 600(seconds).
     * @return The RTM token.
     */
    public String buildToken(String appId, String appCertificate, String userId, int expire) {
        AccessToken accessToken = new AccessToken(appId, appCertificate, expire);
        AccessToken.Service serviceRtm = new AccessToken.ServiceRtm(userId);

        serviceRtm.addPrivilegeRtm(AccessToken.PrivilegeRtm.PRIVILEGE_LOGIN, expire);
        accessToken.addService(serviceRtm);

        try {
            return accessToken.build();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
