package com.estacionamento.park.api.demo.parkapi.web.controller;

import com.estacionamento.park.api.demo.parkapi.entity.Vaga;
import com.estacionamento.park.api.demo.parkapi.service.VagaService;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.VagaCreateDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.VagaResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.mapper.VagaMapper;
import com.estacionamento.park.api.demo.parkapi.web.exception.ErroMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;


    @Operation(summary = "Criar vaga", description = "Recurso para criar vagas",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado"),
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))
                    ,@ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "422", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDto dto){
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.salvar(vaga);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{codigo}").buildAndExpand(vaga.getCodigo())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Recuperar vaga por codigo", description = "Recurso para recuperar vaga por codigo",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))
                    ,@ApiResponse(responseCode = "404", description = "Vaga nao foi encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })

    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDto> getByCodigo(@PathVariable String codigo){
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }

}

