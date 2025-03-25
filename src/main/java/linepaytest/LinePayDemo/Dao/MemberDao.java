package linepaytest.LinePayDemo.Dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import linepaytest.LinePayDemo.Model.Member;

@Mapper
public interface MemberDao {
    
    // 註冊會員帳號
    @Insert("INSERT INTO member (memberName, email, password) VALUES (#{memberName}, #{email}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "memberId", keyColumn = "memberId")
    void register(Member member);

    // 透過 email 取得會員id
    @Select("SELECT memberId FROM member WHERE email = #{email}")
    Integer getMemberIdByEmail(String email);

    // 透過 email 取得會員資料
    @Select("SELECT * FROM member WHERE email = #{email}")
    Member getMemberByEmail(String email);

    // 透過 memberId 取得會員Id
    @Select("SELECT memberId FROM member WHERE memberId = #{memberId}")
    Integer getMemberIdByMemberId(Integer memberId);

    // 透過 memberId 刪除會員資料
    @Delete("DELETE FROM member WHERE memberId = (#{memberId})")
    Boolean deleteMemberById(Integer member_id);
}