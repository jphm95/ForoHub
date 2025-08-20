package com.ForoHub.ForoHub.controller;


import com.ForoHub.ForoHub.infra.security.DatosTokenJWT;
import com.ForoHub.ForoHub.infra.security.TokenService;
import com.ForoHub.ForoHub.usuario.DatosAutentication;
import com.ForoHub.ForoHub.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager manager;

    public AuthenticationController(TokenService tokenService, AuthenticationManager manager) {
        this.tokenService = tokenService;
        this.manager = manager;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<DatosTokenJWT> login(@RequestBody @Valid DatosAutentication datos) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.login(), datos.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}
