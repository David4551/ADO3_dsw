package br.com.carstore.controller;

import br.com.carstore.dto.LoginRequest;
import br.com.carstore.service.JwtTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 1. Tenta autenticar o usuário
        Authentication authentication = authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )

        );

        // 2. Se a autenticação for bem-sucedida, gera o token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtTokenService.generateToken(userDetails);

        // 3. Retorna o token como resposta (pode ser um DTO JSON mais completo)
        return ResponseEntity.ok(jwt);

    }

}
