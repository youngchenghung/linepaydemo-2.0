package linepaytest.LinePayDemo.Model;
import lombok.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private Integer memberId;
    private String memberName;
    private String email;
    private String password;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
