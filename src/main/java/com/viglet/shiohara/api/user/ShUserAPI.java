package com.viglet.shiohara.api.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.persistence.model.user.ShUser;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/user")
@Api(tags="User", description="User API")
public class ShUserAPI {
	
	@Autowired
	ShUserRepository shUserRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<ShUser> shUserList() throws Exception {
		return shUserRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/current")
	public ShUser shUserCurrent(@RequestParam(value = "access_token") int accessToken) throws Exception {
		return shUserRepository.findById(accessToken);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ShUser shUserEdit(@PathVariable int id) throws Exception {	
		return shUserRepository.findById(id);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ShUser shUserUpdate(@PathVariable int id, @RequestBody ShUser shUser) throws Exception {
		ShUser shUserEdit = shUserRepository.findById(id);
		shUserEdit.setConfirmEmail(shUser.getConfirmEmail());
		shUserEdit.setEmail(shUser.getEmail());
		shUserEdit.setFirstName(shUser.getFirstName());
		shUserEdit.setLastLogin(shUser.getLastLogin());
		shUserEdit.setLastName(shUser.getLastName());
		shUserEdit.setLastPostType(shUser.getLastPostType());
		shUserEdit.setLoginTimes(shUser.getLoginTimes());
		shUserEdit.setPassword(shUser.getPassword());
		shUserEdit.setRealm(shUser.getRealm());
		shUserEdit.setRecoverPassword(shUser.getRecoverPassword());
		shUserEdit.setUsername(shUser.getUsername());
		shUserRepository.save(shUserEdit);
		return shUserEdit;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public boolean shUserDelete(@PathVariable int id) throws Exception {
		shUserRepository.delete(id);
		return true;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ShUser shUserAdd(@RequestBody ShUser shUser) throws Exception {
		shUserRepository.save(shUser);
		return shUser;

	}

}
