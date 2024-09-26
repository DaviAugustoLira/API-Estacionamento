package com.estacionamento.park.api.demo.parkapi.repository;

import com.estacionamento.park.api.demo.parkapi.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
