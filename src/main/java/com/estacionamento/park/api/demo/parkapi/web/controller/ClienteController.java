package com.estacionamento.park.api.demo.parkapi.web.controller;

import com.estacionamento.park.api.demo.parkapi.entity.Cliente;
import com.estacionamento.park.api.demo.parkapi.entity.Usuario;
import com.estacionamento.park.api.demo.parkapi.jwt.JwtUserDetail;
import com.estacionamento.park.api.demo.parkapi.repository.projection.ClienteProjection;
import com.estacionamento.park.api.demo.parkapi.service.ClienteService;
import com.estacionamento.park.api.demo.parkapi.service.UsuarioService;
import com.estacionamento.park.api.demo.parkapi.web.dto.ClienteCreateDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.ClienteResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.PageableDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.mapper.ClienteMapper;
import com.estacionamento.park.api.demo.parkapi.web.dto.mapper.PageableMapper;
import com.estacionamento.park.api.demo.parkapi.web.exception.ErroMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;


@Tag(name = "Clientes", description = "Contém todas as operações relacionadas ao recurso de um cliente")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;


    @Operation(summary = "Criar um novo cliente", description = "Recurso para criar um novo cliente",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                @Parameter(in = QUERY, name = "page", content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                description = "Representa a página retornada")
                ,@Parameter(in = QUERY, name = "size", content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                description = "Representa o total de elementos da pagina")
                ,@Parameter(in = QUERY, name = "sort", content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                description = "Representa a ordenação dos resultados")
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))
                    ,@ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "409", description = "Cliente com CPF já cadastrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@Valid @RequestBody ClienteCreateDto dto,
                                                     @AuthenticationPrincipal JwtUserDetail jwtUserDetail) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(jwtUserDetail.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Localizar um cliente", description = "Recurso para localizar um cliente pelo Id",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))
                    ,@ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "404", description = "Cliente nao encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> getById(@PathVariable long id){
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Localizar todos clientes", description = "Recurso para localizar todos os clientes",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                @Parameter(in = QUERY, name = "page",
                    content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                    description = "Representa a pagina retornada"),
                @Parameter(in = QUERY, name = "size",
                        content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                        description = "Representa o total de elementos por página"),
                @Parameter(in = QUERY, name = "sort",
                        content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                        description = "Representa a ordenação dos resultados")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))
                    ,@ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"nome"}
    ) Pageable pageable){
        Page<ClienteProjection> lista = clienteService.buscarTodos(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(lista));
    }

    @Operation(summary = "Recuperar dados do cliente autenticadp", description = "Recurso para recuperar dados do cliente autorizado",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a pagina retornada"),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                            description = "Representa o total de elementos por página"),
                    @Parameter(in = QUERY, name = "sort",
                            content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                            description = "Representa a ordenação dos resultados")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))
                    ,@ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    @GetMapping("/details")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> getDetails(@AuthenticationPrincipal JwtUserDetail jwtUserDetail){
        Cliente cliente = clienteService.buscarPorUsuarioId(jwtUserDetail.getId());
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }
}
