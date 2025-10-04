package com.fiap.sishospitalar.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.sishospitalar.model.Usuario;
import com.fiap.sishospitalar.repository.UsuarioRepository;
import com.fiap.sishospitalar.service.JwtService;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthenticationManager authManager;
	private final JwtService jwtService;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthController(AuthenticationManager authManager, JwtService jwtService, UsuarioRepository usuarioRepository,
			PasswordEncoder passwordEncoder) {
		this.authManager = authManager;
		this.jwtService = jwtService;
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        if (!usuarioRepository.existsByEmail(request.getEmail())) {
        	
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("erro", "Usuário não encontrado");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));
		User user = (User) authentication.getPrincipal();
		String token = jwtService.gerarToken(user);
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", token));
	}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {

        // Verifica se o email já existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
        	
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("erro", "Email já cadastrado");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Usuario savedUser = usuarioRepository.save(usuario);
        savedUser.setSenha(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
	@Getter
	@Setter
	public static class AuthRequest {
		private String email;
		private String senha;
	}
}
