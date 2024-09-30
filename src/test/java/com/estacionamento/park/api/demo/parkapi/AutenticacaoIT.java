package com.estacionamento.park.api.demo.parkapi;

import com.estacionamento.park.api.demo.parkapi.jwt.JwtToken;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioLoginDto;
import com.estacionamento.park.api.demo.parkapi.web.exception.ErroMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuario-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class AutenticacaoIT {

    @Autowired
    WebTestClient webClient;

    @Test
    public void autenticar_comCredenciaisValidas_RetornarTokenComStatus200(){
        JwtToken token = webClient.post().uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(token).isNotNull();
    }

    @Test
    public void autenticar_comCredenciaisErradas_RetornarErrorComStatus400(){
        ErroMessage token = webClient.post().uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("anac@gmail.com", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErroMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(token).isNotNull();
        org.assertj.core.api.Assertions.assertThat(token.getStatus()).isEqualTo(400);

        token = webClient.post().uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("ana@gmail.com", "123457"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErroMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(token).isNotNull();
        org.assertj.core.api.Assertions.assertThat(token.getStatus()).isEqualTo(400);

    }

    @Test
    public void autenticar_comCredenciaisInvalidas_RetornarErrorComStatus400(){
        ErroMessage token = webClient.post().uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("ana@gmail.com", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErroMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(token).isNotNull();
        org.assertj.core.api.Assertions.assertThat(token.getStatus()).isEqualTo(422);

        token = webClient.post().uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErroMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(token).isNotNull();
        org.assertj.core.api.Assertions.assertThat(token.getStatus()).isEqualTo(422);

        token = webClient.post().uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("ana@gmail", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErroMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(token).isNotNull();
        org.assertj.core.api.Assertions.assertThat(token.getStatus()).isEqualTo(422);

        token = webClient.post().uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("ana@gmail.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErroMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(token).isNotNull();
        org.assertj.core.api.Assertions.assertThat(token.getStatus()).isEqualTo(422);

        token = webClient.post().uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErroMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(token).isNotNull();
        org.assertj.core.api.Assertions.assertThat(token.getStatus()).isEqualTo(422);
    }


}
