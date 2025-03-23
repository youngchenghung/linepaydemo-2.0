package linepaytest.LinePayDemo.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.security.Key;

import org.springframework.stereotype.Component;

@Component
public class MyJwtUtil {
    
    // JWT 的密鑰，使用 HS256 算法生成
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // JWT 的過期時間，86400000 毫秒
    private static final long EXPIRATION_TIME = 86400000;

    // 生成一個 JWT 字串，用於驗證使用者身份
    public String generateToken(String email, String authType) {
        return Jwts.builder()
            .setSubject(email)  // 設置 JWT 用戶 (email)
            .claim("authType", authType)  // 自定義 claim "authType"，值為參數 authType
            .setIssuedAt(new Date())  // 設置 JWT 的簽發時間為當前時間
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 設置過期時間為當前時間 + 24 小時
            .signWith(SECRET_KEY)  // 使用 SECRET_KEY 對 JWT 進行簽署，確保其完整性和真實性
            .compact();  // 將 JWT 構建為一個緊湊的字串並返回
    }

    // 從 JWT 中提取用戶的 email
    public String getEmailFromToken(String token) {
        return parseToken(token).getBody().getSubject();  // 解析 JWT 並從其 body 中取（email）
    }

    // 從 JWT 中提取認證類型 (authType)
    public String getAuthTypeFromToken(String token) {
        return parseToken(token).getBody().get("authType", String.class);  // 解析 JWT 並從其 body 中提取 "authType"
    }

    // 解析 JWT 字串
    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()  // 創建 JWT 解析器
            .setSigningKey(SECRET_KEY)  // 設置簽署密鑰，用於驗證 JWT 的簽名
            .build()  // 構建解析器
            .parseClaimsJws(token);  // 解析 JWT，返回包含聲明的 Jws<Claims> 對象
    }

    // 驗證 JWT 是否有效，回傳布林值
    // 返回值：true 表示有效，false 表示無效（過期、簽名錯誤等）
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()  // 創建 JWT 解析器
                .setSigningKey(SECRET_KEY)  // 設置簽署密鑰，用於驗證簽名
                .build()  // 構建解析器
                .parseClaimsJws(token);  // 嘗試解析 JWT，若成功則表示有效
            return true;  // 解析成功，JWT 有效
        } catch (Exception e) {
            return false;  // 解析失敗（例如過期或簽名不匹配），JWT 無效
        }
    }

    // 從 HTTP 請求的 Authorization 頭中提取 JWT 字串
    // 返回值：提取到的 JWT 字串，若無則返回 null
    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");  // 從請求頭中獲取 Authorization 的值
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {  // 檢查是否為 Bearer 類型的 token
            return bearerToken.substring(7);  // 去掉 "Bearer " 前綴（長度為 7），返回純 JWT 字串
        }
        return null;  // 如果 Authorization 頭不存在或格式不正確，返回 null
    }
}
