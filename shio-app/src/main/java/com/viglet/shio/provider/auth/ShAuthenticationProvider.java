package com.viglet.shio.provider.auth;

import org.springframework.security.authentication.AuthenticationProvider;

import com.viglet.shio.persistence.model.auth.ShUser;

public interface ShAuthenticationProvider extends AuthenticationProvider {

	public void init(String providerId);
	
	public ShUser getShUser(String username);
}
