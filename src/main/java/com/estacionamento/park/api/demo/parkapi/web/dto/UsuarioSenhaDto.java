package com.estacionamento.park.api.demo.parkapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UsuarioSenhaDto {

    private String senhaAtual;
    @NotBlank
    @Size(min = 6, max = 6)
    private String novaSenha;
    @NotBlank
    @Size(min = 6, max = 6)
    private String confirmaSenha;
}
