package com.estacionamento.park.api.demo.parkapi.repository;

import com.estacionamento.park.api.demo.parkapi.entity.ClienteVaga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteVagaRespository extends JpaRepository<ClienteVaga, Long> {

}
