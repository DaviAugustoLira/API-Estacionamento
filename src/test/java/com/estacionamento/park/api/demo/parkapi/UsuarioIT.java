package com.estacionamento.park.api.demo.parkapi;

import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioCreateDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioSenhaDto;
import com.estacionamento.park.api.demo.parkapi.web.exception.ErroMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuario-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class UsuarioIT {
    @Autowired
    WebTestClient testClient;

    @Test
    public void createUsuario_ComUsernameEPasswordValidos_RetornarUsuarioCriadoComStatus201(){
        UsuarioResponseDto responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(new UsuarioCreateDto("tody@email.com", "123456"))
                .exchange().expectStatus().isCreated().expectBody(UsuarioResponseDto.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@email.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void createUsuario_ComUsernameInvalido_RetornaErrorMensageStatus422() {
        ErroMessage responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(new UsuarioCreateDto("", "123456"))
                .exchange().expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(new UsuarioCreateDto("tody@", "123456"))
                .exchange().expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(new UsuarioCreateDto("tody@email", "123456"))
                .exchange().expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_ComPasswordInvalido_RetornaErrorMensageStatus422() {
        ErroMessage responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(new UsuarioCreateDto("tody@email.com", "12345"))
                .exchange().expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(new UsuarioCreateDto("tody@email.com", ""))
                .exchange().expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(new UsuarioCreateDto("tody@email.com", " "))
                .exchange().expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(new UsuarioCreateDto("tody@email.com", "1234567"))
                .exchange().expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_ComUsernameRepetido_RetornarUsuarioCriadoComStatus409(){
        ErroMessage responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(new UsuarioCreateDto("ana@gmail.com", "123456"))
                .exchange().expectStatus().isEqualTo(409).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void buscarUsuario_ComIdExistente_RetornarUsuarioCriadoComStatus201(){
        UsuarioResponseDto responseBody = testClient.get().uri("/api/v1/usuarios/1")
                .exchange().expectStatus().isOk().expectBody(UsuarioResponseDto.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");
    }
    @Test
    public void buscarUsuario_ComIdInexistente_RetornarUsuarioCriadoComStatus404(){
        ErroMessage responseBody = testClient.get().uri("/api/v1/usuarios/100")
                .exchange().expectStatus().isNotFound().expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }


    @Test
    public void atualizarSenhaUsuario_RetornarUsuarioCriadoComStatus204(){
        testClient.patch().uri("/api/v1/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "101010", "101010")).exchange().expectStatus()
                .isNoContent();
    }

    @Test
    public void atualizarSenhaUsuario_ComIdInexistente_RetornarUsuarioCriadoComStatus404(){
        ErroMessage responseBody = testClient.patch().uri("/api/v1/usuarios/100").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "101010", "101010")).exchange()
                .expectStatus().isNotFound().expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void atualizarSenhaUsuario_ComSenhaEConfirmacaoDiferentes_RetornarUsuarioCriadoComStatus400(){
        ErroMessage responseBody = testClient.patch().uri("/api/v1/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "101010", "202020")).exchange()
                .expectStatus().isBadRequest().expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void atualizarSenhaUsuario_ComCamposInvalidos_RetornarUsuarioCriadoComStatus400(){
        ErroMessage responseBody = testClient.patch().uri("/api/v1/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("", "", "")).exchange()
                .expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void atualizarSenhaUsuario_ComSenhaAtualErrada_RetornarUsuarioCriadoComStatus400(){
        ErroMessage responseBody = testClient.patch().uri("/api/v1/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123457", "101010", "101010")).exchange()
                .expectStatus().isBadRequest().expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void atualizarSenhaUsuario_ComSenhaAtualENovaIguais_RetornarUsuarioCriadoComStatus400(){
        ErroMessage responseBody = testClient.patch().uri("/api/v1/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "123456", "123456")).exchange()
                .expectStatus().isBadRequest().expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void atualizarSenhaUsuario_SenhasMenoresQue6_RetornarUsuarioCriadoComStatus400(){
        ErroMessage responseBody = testClient.patch().uri("/api/v1/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "12345", "12345")).exchange()
                .expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void atualizarSenhaUsuario_SenhasMaioresQue6_RetornarUsuarioCriadoComStatus400(){
        ErroMessage responseBody = testClient.patch().uri("/api/v1/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "1234567", "1234567")).exchange()
                .expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void buscarTodosOsUsuarios_RetornarStatus200(){
        List<UsuarioResponseDto> responseBody = testClient.get().uri("/api/v1/usuarios").exchange().expectStatus().isOk()
                .expectBodyList(UsuarioResponseDto.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(3);
    }



}
