package com.auth_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.auth_api.domain.user.Users;

public interface UserRepository extends JpaRepository<Users, String> {
	
	UserDetails findByLogin(String login);
	
}
