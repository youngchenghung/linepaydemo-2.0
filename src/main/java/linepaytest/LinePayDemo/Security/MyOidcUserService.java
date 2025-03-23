package linepaytest.LinePayDemo.Security;

import java.sql.Timestamp;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import linepaytest.LinePayDemo.Dao.Oauth2MemberDao;
import linepaytest.LinePayDemo.Model.Oauth2Member;

// 自訂 OidcUserService (Oauth2.0 + OpenID Connect) 社交登入服務 ex.Google
@Component
public class MyOidcUserService extends OidcUserService {

        @Autowired
        private Oauth2MemberDao oauth2MemberDao;

        @Override
        public OidcUser loadUser(OidcUserRequest oidcUserRequest) throws OAuth2AuthenticationException {

            OidcUser oidcUser = super.loadUser(oidcUserRequest);

            System.out.println("Get OAuth2 user：" + oidcUser.getAttributes());

            // 取得使用者的 email 和 name
            String email = Objects.toString(oidcUser.getAttributes().get("email"), null);
            String name = Objects.toString(oidcUser.getAttributes().get("name"), null);

            // 取得 provider 和 providerId
            String provider = oidcUserRequest.getClientRegistration().getRegistrationId();
            String providerId = Objects.toString(oidcUser.getAttributes().get("sub"), null);

            // 取得 accessToken 和 expiresAt
            String accessToken = oidcUserRequest.getAccessToken().getTokenValue();
            Timestamp expiresAt = Timestamp.from(oidcUserRequest.getAccessToken().getExpiresAt());

            // 檢查資料庫中是否已經有這個使用者
            Oauth2Member oauth2Member = oauth2MemberDao.getOauth2Member(provider, providerId);
            // 如果沒有，則新增這個使用者
            if (oauth2Member == null) {
                Oauth2Member newOAuth2Member = new Oauth2Member();
                newOAuth2Member.setProvider(provider);
                newOAuth2Member.setProviderId(providerId);
                newOAuth2Member.setName(name);
                newOAuth2Member.setEmail(email);
                newOAuth2Member.setAccessToken(accessToken);
                newOAuth2Member.setExpiresAt(expiresAt);
                
                oauth2MemberDao.createOauth2Member(newOAuth2Member);
            }

            return oidcUser;

        }
}
