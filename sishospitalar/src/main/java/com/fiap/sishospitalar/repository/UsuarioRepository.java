package com.fiap.sishospitalar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fiap.sishospitalar.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByEmail(String email);
	boolean existsByEmail(String email);
}
