package com.ForoHub.ForoHub.infra.security;

import com.ForoHub.ForoHub.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Filtro Llamado");
        // Bypass auth for login endpoint and CORS preflight
        String path = request.getServletPath();
        if ("/login".equals(path) || "OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        var tokenJWT = recuperarToken(request);
        if (tokenJWT == null) {
            // No token -> continue without authentication; SecurityConfig will enforce rules.
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = usuarioRepository.findByLogin(subject);
            if (usuario == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401: user not found
                return;
            }
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("usuario logeado");
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // Invalid or expired token
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        }
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7); // strip "Bearer "
    }
}
