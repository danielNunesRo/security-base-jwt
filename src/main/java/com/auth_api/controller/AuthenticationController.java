package com.auth_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth_api.domain.user.AuthenticationDTO;
import com.auth_api.domain.user.LoginResponseDTO;
import com.auth_api.domain.user.MessageDTO;
import com.auth_api.domain.user.RegisterDTO;
import com.auth_api.domain.user.Users;
import com.auth_api.infra.security.TokenService;
import com.auth_api.repositories.UserRepository;
import com.auth_api.services.NotificacaoService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	private String exchange;
	
	
	public AuthenticationController(AuthenticationManager authenticationManager, UserRepository repository,
			TokenService tokenService, NotificacaoService notificacaoService,@Value("${rabbitmq.security.exchange}") String exchange) {
		super();
		this.authenticationManager = authenticationManager;
		this.repository = repository;
		this.tokenService = tokenService;
		this.notificacaoService = notificacaoService;
		this.exchange = exchange;
	}

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		
		var token = tokenService.generateToken((Users)auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody RegisterDTO data) {
		if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		Users register = new Users(data.login(), encryptedPassword, data.email(), data.role());
		
		this.repository.save(register);
		MessageDTO msg = new MessageDTO(register.getUsername(), register.getEmail());
		notificacaoService.notificar(msg, exchange);
		
		return ResponseEntity.ok().build();
	}
	
	
}
