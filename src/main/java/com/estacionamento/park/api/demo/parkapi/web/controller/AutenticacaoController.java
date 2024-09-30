package com.estacionamento.park.api.demo.parkapi.web.controller;

import com.estacionamento.park.api.demo.parkapi.jwt.JwtToken;
import com.estacionamento.park.api.demo.parkapi.jwt.JwtUserDetailService;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioLoginDto;
import com.estacionamento.park.api.demo.parkapi.web.exception.ErroMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor

@RequestMapping("/api/v1")
@RestController
public class AutenticacaoController {

    private final JwtUserDetailService detailService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request) {
        log.info("Processo de autenticação pelo login {}", dto.getUsername());
        try{
            UsernamePasswordAuthenticationToken authencationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(authencationToken);
            JwtToken token = detailService.getTokenAuthenticated(dto.getUsername());
            return ResponseEntity.ok(token);
        }catch (AuthenticationException e){
            log.warn("Bad Credentials username {}", dto.getUsername());
        }
        return ResponseEntity.badRequest().body(new ErroMessage(request, HttpStatus.BAD_REQUEST, "Credenciais Invalidas"));
    }
}
