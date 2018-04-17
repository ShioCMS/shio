package com.viglet.shiohara.spring.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.viglet.shiohara.persistence.model.user.ShUser;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;
import com.viglet.shiohara.persistence.repository.user.ShUserRoleRepository;

@Service("customUserDetailsService")
public class ShCustomUserDetailsService implements UserDetailsService {
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShUserRoleRepository shUserRoleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ShUser shUser = shUserRepository.findByUsername(username);
		if (null == shUser) {
			throw new UsernameNotFoundException("No user present with username: " + username);
		} else {
			List<String> userRoles = shUserRoleRepository.findRoleByUsername(username);
			return new ShCustomUserDetails(shUser, userRoles);
		}
	}

}
