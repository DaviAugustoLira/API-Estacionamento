package com.estacionamento.park.api.demo.parkapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class VagaResponseDto {
    private Long id;
    private String codigo;
    private String status;
}
