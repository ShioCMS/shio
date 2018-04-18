package com.viglet.shiohara.api.user;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.persistence.model.user.ShUser;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/user")
@Api(tags = "User", description = "User API")
public class ShUserAPI {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ShUserRepository shUserRepository;

	@GetMapping
	public List<ShUser> shUserList() throws Exception {
		return shUserRepository.findAll();
	}

	@GetMapping("/current")
	public ShUser shUserCurrent() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			ShUser shUser = shUserRepository.findByUsername(currentUserName);
			shUser.setPassword(null);
			return shUser;
		}

		return null;
	}

	@GetMapping("/{id}")
	public ShUser shUserEdit(@PathVariable int id) throws Exception {
		ShUser shUser = shUserRepository.findById(id);
		shUser.setPassword(null);
		return shUserRepository.findById(id);
	}

	@PutMapping("/{id}")
	public ShUser shUserUpdate(@PathVariable int id, @RequestBody ShUser shUser) throws Exception {
		ShUser shUserEdit = shUserRepository.findById(id);
		shUserEdit.setConfirmEmail(shUser.getConfirmEmail());
		shUserEdit.setEmail(shUser.getEmail());
		shUserEdit.setFirstName(shUser.getFirstName());
		shUserEdit.setLastLogin(shUser.getLastLogin());
		shUserEdit.setLastName(shUser.getLastName());
		shUserEdit.setLastPostType(shUser.getLastPostType());
		shUserEdit.setLoginTimes(shUser.getLoginTimes());
		if (shUser.getPassword() != null) {
			shUserEdit.setPassword(passwordEncoder.encode(shUser.getPassword()));
		}
		shUserEdit.setRealm(shUser.getRealm());
		shUserEdit.setRecoverPassword(shUser.getRecoverPassword());
		shUserEdit.setUsername(shUser.getUsername());
		shUserRepository.save(shUserEdit);
		return shUserEdit;
	}

	@DeleteMapping("/{id}")
	public boolean shUserDelete(@PathVariable UUID id) throws Exception {
		shUserRepository.delete(id);
		return true;
	}

	@PostMapping
	public ShUser shUserAdd(@RequestBody ShUser shUser) throws Exception {
		if (shUser.getPassword() != null) {
			shUser.setPassword(passwordEncoder.encode(shUser.getPassword()));
		}
		shUserRepository.save(shUser);
		return shUser;

	}

	@GetMapping("/model")
	public ShUser shUserStructure() throws Exception {
		ShUser shUser = new ShUser();
		return shUser;

	}

}
