package com.estacionamento.park.api.demo.parkapi;

import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioCreateDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.VagaCreateDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.VagaResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.exception.ErroMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vagas/insert-vagas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/delete-vagas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagasIT {
    @Autowired
    WebTestClient testClient;

    @Test
    public void criarVagas_ComRetorno200() {
        testClient.post().uri("/api/v1/vagas")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDto("A-04", "LIVRE"))
                .exchange().expectStatus().isCreated().expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void criarVagas_ComCodigoJaExistente_ComStatus409() {
        testClient.post().uri("/api/v1/vagas")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDto("A-01", "LIVRE"))
                .exchange().expectStatus().isEqualTo(409).expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }

    @Test
    public void criarVagas_ComDadosInvalidos_ComStatus422() {
        testClient.post().uri("/api/v1/vagas")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDto("A-05", "LIBRE"))
                .exchange().expectStatus().isEqualTo(422).expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
        testClient.post().uri("/api/v1/vagas")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDto("A-050", "LIBRE"))
                .exchange().expectStatus().isEqualTo(422).expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
        testClient.post().uri("/api/v1/vagas")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDto("", "LIBRE"))
                .exchange().expectStatus().isEqualTo(422).expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
        testClient.post().uri("/api/v1/vagas")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDto("", ""))
                .exchange().expectStatus().isEqualTo(422).expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }


    @Test
    public void buscaVagaComSucesso_Retorno200() {
        testClient.get().uri("/api/v1/vagas/A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange().expectStatus().isOk().expectBody()
                .jsonPath("id").isEqualTo(1)
                .jsonPath("codigo").isEqualTo("A-01")
                .jsonPath("status").isEqualTo("LIVRE");
    }

    @Test
    public void buscaVagaNotFound_Retorno404() {
        testClient.get().uri("/api/v1/vagas/A-05")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange().expectStatus().isEqualTo(404).expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vagas/A-05");
    }

    @Test
    public void criaVaga_SemPermissao_Retorno403() {
        testClient.post().uri("/api/v1/vagas")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "julia@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDto("A-05", "LIVRE"))
                .exchange().expectStatus().isEqualTo(403).expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }

    @Test
    public void buscaVagaSemPermissao_Retorno403() {
        testClient.get().uri("/api/v1/vagas/A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "julia@gmail.com", "123456"))
                .exchange().expectStatus().isEqualTo(403).expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("GET");
    }




}
