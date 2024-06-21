package rostov.tuskarora.app.Config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rostov.tuskarora.app.Model.Teacher;

import java.util.Date;

@Component
public class JwtCore {
    @Value("${Tuskarora.app.secret}")
    private String secret;
    @Value("${Tuskarora.app.lifetime}")
    private int lifetime;
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", ((Teacher) userDetails).getId())
                .claim("username", userDetails.getUsername())
                .claim("email", ((Teacher) userDetails).getEmail())
                .claim("post", ((Teacher) userDetails).getPost())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime() + lifetime)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    public String getNameFromJwt(String token) {
        return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
    }
}
