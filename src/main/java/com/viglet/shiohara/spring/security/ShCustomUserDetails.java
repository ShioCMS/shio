package com.viglet.shiohara.spring.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.viglet.shiohara.persistence.model.user.ShUser;

public class ShCustomUserDetails extends ShUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	private List<String> shUserRoles;

	public ShCustomUserDetails(ShUser shUser, List<String> shUserRoles) {
		super(shUser);
		this.shUserRoles = shUserRoles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		String roles = StringUtils.collectionToCommaDelimitedString(shUserRoles);
		return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getUsername() {
		return super.getUsername();
	}

}
