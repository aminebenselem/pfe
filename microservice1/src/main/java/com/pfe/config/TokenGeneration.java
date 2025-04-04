package com.pfe.config;


import com.pfe.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenGeneration {
    private UserService userService;

    @Value(value = "${jwt.secret}")
    private String TOKEN_SECRET;

    public TokenGeneration(UserService userService) {
        this.userService = userService;
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims=new HashMap<>();
        String CLAIMS_USER = "sub";
        String CLAIMS_ROLE="role";
        claims.put(CLAIMS_USER, userDetails.getUsername());
        claims.put(CLAIMS_ROLE,userDetails.getAuthorities());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration (generateExpirationDate())
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET)
                .compact();

    }
    public String getUserNameFromToken(String token){
        try {
            Claims claims = getClaims(token);
            return claims.getSubject();
        }catch (Exception ex) {
            return null;
        }
    }
    private Date generateExpirationDate() {
        long TOKEN_VALIDITY = 604800L;
        return new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000);
    }


    public boolean isTokenValid (String token, UserDetails userDetails) {
        String username = getUserNameFromToken (token) ;
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired (String token) {
        Date expiration = getClaims (token).getExpiration();
        return expiration. before(new Date());
    }
    private Claims getClaims (String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey (TOKEN_SECRET)
                    .parseClaimsJws (token)
                    .getBody();
        }catch (Exception ex) {
            claims = null;
        }
        return claims;

    }
}
