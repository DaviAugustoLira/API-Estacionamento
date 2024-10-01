package com.estacionamento.park.api.demo.parkapi;

import com.estacionamento.park.api.demo.parkapi.web.dto.ClienteCreateDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.ClienteResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.PageableDto;
import com.estacionamento.park.api.demo.parkapi.web.exception.ErroMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/insert-clientes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/delete-clientes.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {

    @Autowired
    WebTestClient client;

    @Test
    public void criarCliente_ComDadosValidos_ComRetorno201(){
        ClienteResponseDto responseBody = client.post().uri("/api/v1/clientes").contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "julia@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDto("Juliana", "87837530012")).exchange()
                .expectStatus().isCreated().expectBody(ClienteResponseDto.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Juliana");
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("87837530012");
    }

    @Test
    public void criarCliente_ComCpfJaCadastrado_ComRetorno409() {
        ErroMessage responseBody = client.post().uri("/api/v1/clientes").contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "julia@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDto("Juliana", "51200313070")).exchange()
                .expectStatus().isEqualTo(409).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void criarCliente_ComCpfInvalido_ComRetorno422() {
        ErroMessage responseBody = client.post().uri("/api/v1/clientes").contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "julia@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDto("Juliana", "51200313070000")).exchange()
                .expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void criarCliente_ComNomeInvalido_ComRetorno422() {
        ErroMessage responseBody = client.post().uri("/api/v1/clientes").contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "julia@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDto("Ju", "51200313070")).exchange()
                .expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void criarCliente_ComTodosDadosInvalidos_ComRetorno422() {
        ErroMessage responseBody = client.post().uri("/api/v1/clientes").contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "julia@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDto("Ju", "00000000000")).exchange()
                .expectStatus().isEqualTo(422).expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void criarCliente_ComUsuarioNaoPermitido_ComRetorno403() {
        ErroMessage responseBody = client.post().uri("/api/v1/clientes").contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "ana@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDto("Juliana", "51200313070")).exchange()
                .expectStatus().isForbidden().expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void buscarCliente_ComSucessonaOperacao_Retorno200() {
        ClienteResponseDto responseBody = client.get().uri("/api/v1/clientes/2")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk().expectBody(ClienteResponseDto.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(2);
    }

    @Test
    public void buscarCliente_ComIdInexistente_Retorno404() {
        ErroMessage responseBody = client.get().uri("/api/v1/clientes/10")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound().expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void buscarCliente_SemPermissao_Retorno403() {
        ErroMessage responseBody = client.get().uri("/api/v1/clientes/10")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden().expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void BuscarClientesCompaginacaoPeloAdminPagina1_Status200(){
        PageableDto responseBody = client.get().uri("/api/v1/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "ana@gmail.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);
    }
    @Test
    public void BuscarClientesCompaginacaoPeloAdminPagina2_Status200(){
        PageableDto responseBody = client.get().uri("/api/v1/clientes?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "ana@gmail.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void BuscarClientesCompaginacaoPeloCliente_Status200() {
        ErroMessage responseBody = client.get().uri("/api/v1/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@gmail.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void BuscarRegistroDoClienteAutenticadoNaPlataforma() {
        ClienteResponseDto responseBody = client.get().uri("/api/v1/clientes/details")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@gmail.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(ClienteResponseDto.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(2);

    }

    @Test
    public void BuscarRegistroDoClienteAutenticadoNaPlataformaComoAdmin() {
        ErroMessage responseBody = client.get().uri("/api/v1/clientes/details")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "ana@gmail.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(ErroMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

}
