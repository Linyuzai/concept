package $PACKAGE$.token.jwt;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.token.TokenCodec;
import com.github.linyuzai.domain.core.DomainFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT token 编解码实现
 */
@Getter
@Component
public class JwtTokenCodec implements TokenCodec {

    public static final String ID = "id";

    private final String KEY = "$ARTIFACT$";

    @Autowired
    private DomainFactory factory;

    @Override
    public String encode(User user) {
        byte[] encodeKey = getEncodedKey();
        SecretKey key = new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID, user.getId());
        return Jwts.builder()
                .setIssuedAt(new Date())
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    @Override
    public User decode(String token) {
        Claims claims = Jwts.parser().setSigningKey(getEncodedKey()).parseClaimsJws(token).getBody();
        String userId = claims.get(ID, String.class);
        return factory.createObject(User.class, userId);
    }

    protected byte[] getEncodedKey() {
        return Base64.getEncoder().encode(KEY.getBytes(StandardCharsets.UTF_8));
    }
}
