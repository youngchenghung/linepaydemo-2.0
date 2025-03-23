package linepaytest.LinePayDemo.Dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import linepaytest.LinePayDemo.Model.Oauth2Member;
import linepaytest.LinePayDemo.RowMapper.Oauth2MemberRowMapper;

@Component
public class Oauth2MemberDaoImpl implements Oauth2MemberDao {
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private Oauth2MemberRowMapper oauth2MemberRowMapper;

    // provider 取得 Oauth2 會員
    @Override
    public Oauth2Member getOauth2Member(String provider, String providerId) {
        String sql = """
                SELECT oauth2_member_id, provider, provider_id, name, email, access_token, expires_at
                FROM oauth2_member
                WHERE provider = :provider AND provider_id = :providerId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("provider", provider);
        map.put("providerId", providerId);

        List<Oauth2Member> oauth2MemberList = namedParameterJdbcTemplate.query(sql, map, oauth2MemberRowMapper);

        if (oauth2MemberList.size() > 0) {
            return oauth2MemberList.get(0);
        } else {
            return null;
        }
    }

    // 建立 Oauth2 會員
    @Override
    public Integer createOauth2Member(Oauth2Member oauth2Member) {
        String sql = """
                INSERT INTO oauth2_member(provider, provider_id, name, email, access_token, expires_at)
                VALUES (:provider, :providerId, :name, :email, :accessToken, :expiresAt)
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("provider", oauth2Member.getProvider());
        map.put("providerId", oauth2Member.getProviderId());
        map.put("name", oauth2Member.getName());
        map.put("email", oauth2Member.getEmail());
        map.put("accessToken", oauth2Member.getAccessToken());
        map.put("expiresAt", oauth2Member.getExpiresAt());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int oauth2MemberId = keyHolder.getKey().intValue();

        return oauth2MemberId;
    }

    // 透過 Email 取得 Oauth2 會員
    @Override
    public Oauth2Member getOauth2MemberByEmail(String email) {
        String sql = """
                SELECT oauth2_member_id, provider, provider_id, name, email, access_token, expires_at
                From oauth2_member
                WHERE email = :email
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<Oauth2Member> oauth2MemberList = namedParameterJdbcTemplate.query(sql, map, oauth2MemberRowMapper);

        return oauth2MemberList.isEmpty() ? null : oauth2MemberList.get(0);
    }
}