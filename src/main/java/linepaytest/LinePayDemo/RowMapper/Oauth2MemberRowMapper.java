package linepaytest.LinePayDemo.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import linepaytest.LinePayDemo.Model.Oauth2Member;

@Component
public class Oauth2MemberRowMapper implements RowMapper<Oauth2Member> {

    @Override
    public Oauth2Member mapRow(ResultSet resultSet, int i) throws SQLException {

        Oauth2Member oauth2Member = new Oauth2Member();
        oauth2Member.setOauth2memberId(resultSet.getInt("oauth2_member_id"));
        oauth2Member.setProvider(resultSet.getString("provider"));
        oauth2Member.setProviderId(resultSet.getString("provider_id"));
        oauth2Member.setName(resultSet.getString("name"));
        oauth2Member.setEmail(resultSet.getString("email"));
        oauth2Member.setAccessToken(resultSet.getString("access_token"));
        oauth2Member.setExpiresAt(resultSet.getTimestamp("expires_at"));

        return oauth2Member;
    }
}
