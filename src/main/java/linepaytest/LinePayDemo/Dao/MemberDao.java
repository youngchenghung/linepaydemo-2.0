package linepaytest.LinePayDemo.Dao;

import linepaytest.LinePayDemo.Model.Member;

public interface MemberDao {
    
    // 註冊會員帳號
    Integer register(Member member);

    // 透過 email 取得會員
    Integer getMemberIdByEmail(String email);

    // 透過 email 取得會員
    Member getMemberByEmail(String email);
}
