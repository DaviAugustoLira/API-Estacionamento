package com.estacionamento.park.api.demo.parkapi.web.controller;

import com.estacionamento.park.api.demo.parkapi.entity.ClienteVaga;
import com.estacionamento.park.api.demo.parkapi.service.EstacionamentoService;
import com.estacionamento.park.api.demo.parkapi.web.dto.EstacionamentoCreateDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.EstacionamentoResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.UsuarioResponseDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.mapper.ClienteVagaMapper;
import com.estacionamento.park.api.demo.parkapi.web.exception.ErroMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name="Estacionamento", description = "Contém todas as operações relativas a estacionamento")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/estacionamentos")
public class EstacionamentoController {
    private final EstacionamentoService estacionamentoService;

    @Operation(summary = "Registrar check-in", description = "Recurso para realizar um check-in",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))
                    ,@ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "404", description = "Cliente nao encontrado ou nenhuma vaga livre",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
                    ,@ApiResponse(responseCode = "422", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroMessage.class)))
            })
    @PostMapping("/checkin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkin(@RequestBody @Valid EstacionamentoCreateDto dto){
        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);
        EstacionamentoResponseDto responseDto = ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }
}
