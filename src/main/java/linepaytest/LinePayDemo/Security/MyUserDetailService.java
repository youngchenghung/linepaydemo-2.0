package linepaytest.LinePayDemo.Security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import linepaytest.LinePayDemo.Dao.MemberDao;
import linepaytest.LinePayDemo.Model.Member;

@Component
public class MyUserDetailService implements UserDetailsService {
    
    private final MemberDao memberDao;
    
    public MyUserDetailService(MemberDao memberDao){
        this.memberDao = memberDao;
    }

    // loadUserByUsername 方法會回傳一個 UserDetails 物件，這個 UserDetails 物件是用來驗證使用者身份
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberDao.getMemberByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new User(member.getEmail(), member.getPassword(), new ArrayList<>());
    }
    
}
