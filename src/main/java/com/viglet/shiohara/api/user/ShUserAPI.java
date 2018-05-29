package com.viglet.shiohara.api.user;

import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
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
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShUser> shUserList() throws Exception {
		return shUserRepository.findAll();
	}

	@GetMapping("/current")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
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
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShUser shUserEdit(@PathVariable int id) throws Exception {
		ShUser shUser = shUserRepository.findById(id);
		shUser.setPassword(null);
		return shUserRepository.findById(id);
	}

	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShUser shUserUpdate(@PathVariable int id, @RequestBody ShUser shUser) throws Exception {
		if (shUser.getPassword() != null) {
			shUser.setPassword(passwordEncoder.encode(shUser.getPassword()));
		}	
		shUserRepository.save(shUser);
		return shUser;
	}

	@DeleteMapping("/{id}")
	public boolean shUserDelete(@PathVariable int id) throws Exception {
		shUserRepository.delete(id);
		return true;
	}

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShUser shUserAdd(@RequestBody ShUser shUser) throws Exception {
		if (shUser.getPassword() != null) {
			shUser.setPassword(passwordEncoder.encode(shUser.getPassword()));
		}
		
		shUserRepository.save(shUser);
		
		return shUser;
	}

	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShUser shUserStructure() throws Exception {
		ShUser shUser = new ShUser();
		return shUser;

	}

}
