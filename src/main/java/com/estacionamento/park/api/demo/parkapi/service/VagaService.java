package com.estacionamento.park.api.demo.parkapi.service;

import com.estacionamento.park.api.demo.parkapi.entity.Vaga;
import com.estacionamento.park.api.demo.parkapi.exception.CodigoUniqueViolationException;
import com.estacionamento.park.api.demo.parkapi.exception.EntityNotFoundException;
import com.estacionamento.park.api.demo.parkapi.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.estacionamento.park.api.demo.parkapi.entity.Vaga.StatusVaga.LIVRE;

@RequiredArgsConstructor
@Service
public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar(Vaga vaga){
        try{
            return vagaRepository.save(vaga);
        }catch (DataIntegrityViolationException ex){
            throw new CodigoUniqueViolationException(String.format("Vaga com codigo %s ja cadastrada", vaga.getCodigo()));
        }
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo(String codigo){
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException(String.format("vaga com codigo %snao foi encontrada", codigo))
        );
    }
    @Transactional(readOnly = true)
    public Vaga buscarPorVagaLivre() {
        return vagaRepository.findFirstByStatus(LIVRE).orElseThrow(
                () -> new EntityNotFoundException(String.format("Nenhuma vaga livre foi encontrada"))
        );
    }
}
