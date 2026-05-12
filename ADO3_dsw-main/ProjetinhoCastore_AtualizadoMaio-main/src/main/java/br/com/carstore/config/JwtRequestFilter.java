package br.com.carstore.config;

import br.com.carstore.service.JwtTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService; // O mesmo usado no LAB 8 (autenticação in-memory)
    private final JwtTokenService jwtTokenService;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtTokenService jwtTokenService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 1. Extrai o token do cabeçalho
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            jwt = authorizationHeader.substring(7);

            try {
                username = jwtTokenService.extractUsername(jwt);
            } catch (Exception e) {
                logger.warn("JWT Token inválido ou expirado.");
                // Continua o filtro; a exceção de autenticação será lançada depois.
            }

        }

        // 2. Valida o token e define a autenticação no contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenService.validateToken(jwt, userDetails)) {

                // Cria o objeto de autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Define o usuário no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }

        filterChain.doFilter(request, response);

    }

}
