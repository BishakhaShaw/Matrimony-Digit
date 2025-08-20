//package digit.matrimony.security.jwt;//package digit.matrimony.security.jwt;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class JwtUtil {
//
//    private final SecretKey key;
//
//    private final int expirationMinutes;
//
//    public JwtUtil(
//            @Value("${security.jwt.secret}") String secret,
//            @Value("${security.jwt.expiration-minutes}") int expirationMinutes
//    ) {
//        this.key = Keys.hmacShaKeyFor(secret.getBytes());
//        this.expirationMinutes = expirationMinutes;
//    }
//
//    public String generateToken(String subject, List<String> roles) {
//        Instant now = Instant.now();
//        return Jwts.builder()
//                .setSubject(subject) // username or email
//                .addClaims(Map.of("roles", roles))
//                .setIssuedAt(Date.from(now))
//                .setExpiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public Jws<Claims> parse(String token) {
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//    }
//
//    public boolean isValid(String token) {
//        try {
//            parse(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    public String getSubject(String token) {
//        return parse(token).getBody().getSubject();
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<String> getRoles(String token) {
//        Object roles = parse(token).getBody().get("roles");
//        return roles instanceof List ? (List<String>) roles : List.of();
//    }
//
//    public Date getExpiration(String token) {
//        return parse(token).getBody().getExpiration();
//    }
//}






















package digit.matrimony.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 86400000; // 1 day

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> getRoles(String token) {
        return (List<String>) Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles");
    }

    public Date getExpiration(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
