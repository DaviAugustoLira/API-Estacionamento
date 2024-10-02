package com.estacionamento.park.api.demo.parkapi.web.dto.mapper;

import com.estacionamento.park.api.demo.parkapi.entity.ClienteVaga;
import com.estacionamento.park.api.demo.parkapi.web.dto.EstacionamentoCreateDto;
import com.estacionamento.park.api.demo.parkapi.web.dto.EstacionamentoResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ClienteVagaMapper {

    public static ClienteVaga toClienteVaga(EstacionamentoCreateDto dto){
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    public static EstacionamentoResponseDto toDto(ClienteVaga cliente){
        return new ModelMapper().map(cliente, EstacionamentoResponseDto.class);
    }
}
