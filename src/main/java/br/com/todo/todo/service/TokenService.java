package br.com.todo.todo.service;

import br.com.todo.todo.model.Usuario;
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
    private String expiracaoMillis;

    @Value("${todo.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication usuarioAutenticado) {
        Usuario userLogado = (Usuario) usuarioAutenticado.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiracaoMillis));

        return Jwts.builder()
                .setIssuer("API todo")
                .setSubject(userLogado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public boolean validarToken(String token) {
        try{
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Long getIdUsuario(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();

        return Long.valueOf(claims.getSubject());
    }
}
