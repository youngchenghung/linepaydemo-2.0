package linepaytest.LinePayDemo.Dao;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import linepaytest.LinePayDemo.RowMapper.MemberRowMapper;
import linepaytest.LinePayDemo.Model.Member;

@Component
public class MemberDaoImpl implements MemberDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 註冊會員帳號
    @Override
    public Integer register (Member member) {
        String sql = "INSERT INTO member (name, email, password) VALUES (:memberName, :email, :password)";

        // 設定 SQL 參數
        Map<String, Object> map = new HashMap<>();
        map.put("memberName", member.getMemberName());
        map.put("email", member.getEmail());
        map.put("password", member.getPassword());

        // 取得自動生成的 memberId
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 執行 SQL update 更新資料
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        // 取得 memberId
        Integer memberId = keyHolder.getKey().intValue();

        return memberId;
    }
    
    // 透過 email 取得會員ID
    @Override
    public Integer getMemberIdByEmail(String email) {
        String sql = "SELECT member_id FROM member WHERE email = :email";

        // 設定 SQL 參數
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        try {
        // 執行 SQL queryForObject 查詢單一物件
            Integer memberId = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

            return memberId;
        } 
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 透過 email 取得會員
    @Override
    public Member getMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = :email";

        // 設定 SQL 參數
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        try {
            // 執行 SQL queryForObject 查詢單一物件
            Member member = namedParameterJdbcTemplate.queryForObject(sql, map, new MemberRowMapper());

            return member;
        } 
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
