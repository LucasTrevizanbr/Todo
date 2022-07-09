package br.com.todo.infraestructure.security.token;

import br.com.todo.domain.repository.UserRepository;
import br.com.todo.domain.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository usuarioRepository;

    public AuthenticationTokenFilter(TokenService tokenService, UserRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = retrieveToken(request);
        boolean valid = tokenService.validateToken(token);

        if(valid){
            authenticateUser(token);
        }

        filterChain.doFilter(request, response);

    }

    private void authenticateUser(String token) {
        Long idUser = tokenService.getIdUser(token);
        User user = usuarioRepository.findById(idUser).get();

        UsernamePasswordAuthenticationToken authenticated =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticated);
    }

    private String retrieveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if(token == null || !token.startsWith("Bearer ")){
            return null;
        }

        return token.substring(7);
    }
}
