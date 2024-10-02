package com.estacionamento.park.api.demo.parkapi.repository;

import com.estacionamento.park.api.demo.parkapi.entity.Cliente;
import com.estacionamento.park.api.demo.parkapi.repository.projection.ClienteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("select c from Cliente c")
    Page<ClienteProjection> finAllPageable(Pageable pageable);

    Cliente findByUsuarioId(long id);

    Optional<Cliente> findByCpf(String cpf);
}
