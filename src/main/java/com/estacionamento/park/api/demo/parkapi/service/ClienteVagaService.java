package com.estacionamento.park.api.demo.parkapi.service;

import com.estacionamento.park.api.demo.parkapi.entity.ClienteVaga;
import com.estacionamento.park.api.demo.parkapi.repository.ClienteVagaRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {
    private final ClienteVagaRespository clienteVagaRepository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga) {
        return clienteVagaRepository.save(clienteVaga);
    }
}
