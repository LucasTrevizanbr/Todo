package br.com.todo.infraestructure.security.token;

import br.com.todo.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${todo.jwt.expiration}")
    private String expirationTimeInMillis;

    @Value("${todo.jwt.secret}")
    private String secret;

    public String generateTokenToUserAuthenticated(Authentication userAuthenticated) {
        User loggedUser = (User) userAuthenticated.getPrincipal();
        Date today = new Date();
        Date expirationDate = new Date(today.getTime() + Long.parseLong(expirationTimeInMillis));

        return Jwts.builder()
                .setIssuer("API todo")
                .setSubject(loggedUser.getId().toString())
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Long getIdUser(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();

        return Long.valueOf(claims.getSubject());
    }
}
