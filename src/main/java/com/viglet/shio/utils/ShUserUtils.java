package com.viglet.shio.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.viglet.shio.persistence.model.auth.ShUser;
import com.viglet.shio.persistence.repository.auth.ShUserRepository;

@Component
public class ShUserUtils {

	@Autowired
	private ShUserRepository shUserRepository;

	public boolean isValidUserAndPassword(String username, String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		ShUser shUser = shUserRepository.findByUsername(username);

		return (shUser != null && passwordEncoder.matches(password, shUser.getPassword())) ? true : false;
	}
}
