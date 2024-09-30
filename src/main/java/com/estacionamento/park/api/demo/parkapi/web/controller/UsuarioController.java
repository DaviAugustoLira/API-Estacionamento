package com.estacionamento.park.api.demo.parkapi.web.controller;

import com.estacionamento.park.api.demo.parkapi.entity.Usuario;
import com.estacionamento.park.api.demo.parkapi.service.UsuarioService;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioCreateDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioSenhaDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.mapper.UsuarioMapper;
import com.estacionamento.park.api.demo.parkapi.web.exception.ErroMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Usuarios", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo usuário", description = "Recurso para criar um novo usuário",
    responses = {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))
                ,@ApiResponse(responseCode = "403", description = "Usuario sem permissão para acessar este recurso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "409", description = "Usuário e-mail ja cadastrado no sistema",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto) {
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Buscar usuário pelo ID", description = "Recurso para buscar um usuário na base de dados pelo ID",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))
                    ,@ApiResponse(responseCode = "403", description = "Usuario sem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "404", description = "Registro não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<UsuarioResponseDto> getById (@PathVariable long id) {
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Atualizar senha de um usuário", description = "Recurso para atualizar senha",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso")
                    ,@ApiResponse(responseCode = "403", description = "Usuario sem permissao para acessar este recurso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "400", description = "Senha não confere",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "422", description = "Campos invalidos ou mal formatados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN, CLIENTE') OR (hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<Void> update(@PathVariable long id, @Valid @RequestBody UsuarioSenhaDto dto) {
        usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar todos os usuários na base de dados", description = "Recurso para buscar todos os usuários da base de dados",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "200", description = "Busca realuzada com sucesso",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class))))
            ,@ApiResponse(responseCode = "403", description = "Usuario sem permissão para acessar este recurso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> getAll(){
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    }

}
