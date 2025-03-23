package linepaytest.LinePayDemo.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import linepaytest.LinePayDemo.Model.Member;

public class MemberRowMapper implements RowMapper<Member> {
    
    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setMemberName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPassword(rs.getString("password"));
        return member;
    }
    
}
