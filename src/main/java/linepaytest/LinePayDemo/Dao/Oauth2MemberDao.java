package linepaytest.LinePayDemo.Dao;

import linepaytest.LinePayDemo.Model.Oauth2Member;

public interface Oauth2MemberDao {
    
    // provider 取得 Oauth2 會員
    Oauth2Member getOauth2Member(String provider, String providerId);

    // 建立 Oauth2 會員
    Integer createOauth2Member(Oauth2Member oauth2Member);

    // 透過 Email 取得 Oauth2 會員
    Oauth2Member getOauth2MemberByEmail(String email);
}
