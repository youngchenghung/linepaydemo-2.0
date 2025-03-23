package linepaytest.LinePayDemo.Model;

import java.sql.Timestamp;

public class Oauth2Member {
    
    private Integer oauth2memberId;
    private String provider;
    private String providerId;
    private String name;
    private String email;
    private String accessToken;
    private Timestamp expiresAt;

    public Integer getOauth2memberId() {
        return oauth2memberId;
    }
    public void setOauth2memberId(Integer oauth2memberId) {
        this.oauth2memberId = oauth2memberId;
    }
    public String getProvider() {
        return provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public String getProviderId() {
        return providerId;
    }
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public Timestamp getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }
}
