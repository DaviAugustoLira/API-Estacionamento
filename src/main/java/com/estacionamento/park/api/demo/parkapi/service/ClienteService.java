package com.estacionamento.park.api.demo.parkapi.service;

import com.estacionamento.park.api.demo.parkapi.entity.Cliente;
import com.estacionamento.park.api.demo.parkapi.exception.CpfUniqueViolationException;
import com.estacionamento.park.api.demo.parkapi.exception.EntityNotFoundException;
import com.estacionamento.park.api.demo.parkapi.repository.ClienteRepository;
import com.estacionamento.park.api.demo.parkapi.repository.projection.ClienteProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        try{
            return clienteRepository.save(cliente);
        }catch (DataIntegrityViolationException e){
            throw new CpfUniqueViolationException(String.format("CPF %s não pode ser cadastrado, já existe no sistema", cliente.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(long id) {
        return clienteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Client Id %s nao encontrado no sistema", id))
        );
    }

    @Transactional(readOnly = true)
    public Page<ClienteProjection> buscarTodos(Pageable pageable) {
        return clienteRepository.finAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorUsuarioId(long id) {
        return clienteRepository.findByUsuarioId(id);
    }
}
