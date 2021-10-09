package com.viglet.shio.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.viglet.shio.persistence.model.auth.ShUser;
import com.viglet.shio.persistence.repository.auth.ShUserRepository;
import com.viglet.shio.spring.security.auth.IShAuthenticationFacade;

@Component
public class ShUserUtils {

	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private IShAuthenticationFacade authenticationFacade;

	public String getCurrentUsername() {
		Authentication authentication = authenticationFacade.getAuthentication();
		return authentication.getName();
	}

	public boolean isValidUserAndPassword(String username, String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		ShUser shUser = shUserRepository.findByUsername(username);
		return (shUser != null && passwordEncoder.matches(password, shUser.getPassword()));
	}
}
