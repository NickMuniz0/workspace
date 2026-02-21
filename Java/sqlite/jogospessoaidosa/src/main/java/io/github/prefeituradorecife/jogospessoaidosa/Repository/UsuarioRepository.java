package io.github.prefeituradorecife.jogospessoaidosa.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    
}