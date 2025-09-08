package com.novatronic.masivas.backoffice.security.util;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.List;

/**
 *
 * @author Obi Consulting
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final HttpServletRequest request;

    public JwtUtil(HttpServletRequest request) {
        this.request = request;
    }

    public String generateToken(String username, long expirationTime) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String generateToken(String username, List<String> scope, String profile, int tiempoExpiracion) {

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("scopes", scope.stream().map(s -> s.toString()).toList());
        claims.put("ip", clientIp);
        claims.put("tiempoSesion", tiempoExpiracion / 6);

        Date now = new Date();
        Date validity = new Date(now.getTime() + tiempoExpiracion * 60_000L);

        String tokenId = UUID.randomUUID().toString();
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setAudience(profile)
                .setId(tokenId)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256) // Usar clave válida
                .compact();
    }

    public boolean validateToken(String token, String username) {
        String tokenUsername = extractUsername(token);
        boolean isTokenExpired = isTokenExpired(token);
        return (tokenUsername.equals(username) && !isTokenExpired);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractAllClaims(token).getExpiration();
        return expirationDate.before(new Date());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

}
