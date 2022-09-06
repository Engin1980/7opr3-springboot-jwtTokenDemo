package cz.osu.kip.jwttokendemo;

import java.io.Serial;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

  public static final long JWT_TOKEN_VALIDITY = 15 * 60;

  @Value("${jwt.secret}") private String secretKey;

  private final Key key;

  public JwtTokenUtil(){
    // key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    key = Keys.secretKeyFor(SignatureAlgorithm.HS512); //or HS384 or HS256
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> customClaims = new HashMap<>();
    String rolesString = userDetails.getAuthorities().stream()
            .map(q->q.toString())
            .collect(Collectors.joining(","));
    customClaims.put("roles", rolesString);

    return Jwts.builder()
            .setClaims(customClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(key)
            .compact();
  }

  public Claims getClaimsFromToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  public String[] getRolesFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    String rolesString = claims.get("roles", String.class);
    return rolesString == null ? new String[0] : rolesString.split(",");
  }

  public Boolean isTokenValid(String token) {
    getClaimsFromToken(token); // throws an exception in the case of an issue
    return true;
  }
}
