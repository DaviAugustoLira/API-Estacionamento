package com.estacionamento.park.api.demo.parkapi.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UsuarioResponseDto {

    private long id;
    private String username;
    private String role;

}
