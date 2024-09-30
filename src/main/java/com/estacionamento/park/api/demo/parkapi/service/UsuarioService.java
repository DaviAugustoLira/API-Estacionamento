package com.estacionamento.park.api.demo.parkapi.service;

import com.estacionamento.park.api.demo.parkapi.entity.Usuario;
import com.estacionamento.park.api.demo.parkapi.exception.EntityNotFoundException;
import com.estacionamento.park.api.demo.parkapi.exception.PasswordInvalidException;
import com.estacionamento.park.api.demo.parkapi.exception.UsernameUniqueViolationException;
import com.estacionamento.park.api.demo.parkapi.repository.UsuarioRepository;
import io.jsonwebtoken.security.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        try{
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        }catch(DataIntegrityViolationException e){
            throw new UsernameUniqueViolationException(String.format("Username %s ja cadastrado", usuario.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () ->new EntityNotFoundException(String.format("Usuario id %s não encontrado", id)));
    }

    @Transactional
    public Usuario editarSenha(long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        Usuario usuario = buscarPorId(id);
        if(!passwordEncoder.matches(senhaAtual, usuario.getPassword())) {
            throw new PasswordInvalidException("Senha errada");
        }
        if(!novaSenha.equals(confirmaSenha)) {
            throw new PasswordInvalidException("Nova senha não confere com confirmação de senha");
        }
        if(usuario.getPassword().equals(novaSenha)) {
            throw new PasswordInvalidException("Nova senha é igual a senha antiga");
        }
        usuario.setPassword(passwordEncoder.encode(novaSenha));
        return usuario;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () ->new EntityNotFoundException(String.format("Usuario com username %s não encontrado", username)));
    }

    @Transactional(readOnly = true)
    public Usuario.Role buscarRolePorUsername(String username) {
        return usuarioRepository.findByRoleUsername(username);
    }
}
