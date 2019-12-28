/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.viglet.shiohara.api.provider.auth;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.viglet.shiohara.bean.provider.auth.ShAuthProviderInstanceBean;
import com.viglet.shiohara.persistence.model.provider.auth.ShAuthProviderInstance;
import com.viglet.shiohara.persistence.model.provider.auth.ShAuthProviderVendor;
import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.repository.provider.auth.ShAuthProviderInstanceRepository;
import com.viglet.shiohara.persistence.repository.provider.auth.ShAuthProviderVendorRepository;
import com.viglet.shiohara.persistence.repository.system.ShConfigVarRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v2/provider/auth")
@Api(tags = "Auth Provider", description = "Auth Provider API")
public class ShAuthProviderAPI {
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ShAuthProviderAPI.class);

	private static final String PROVIDER_PATH = "/provider/%s";

	@Autowired
	private ShAuthProviderInstanceRepository shAuthProviderInstanceRepository;
	@Autowired
	private ShAuthProviderVendorRepository shAuthProviderVendorRepository;
	@Autowired
	private ShConfigVarRepository shConfigVarRepository;

	@GetMapping("/vendor")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShAuthProviderVendor> shAuthProviderVendorListItem() {
		return shAuthProviderVendorRepository.findAll();
	}

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShAuthProviderInstance> shAuthProviderInstanceListItem() {
		return shAuthProviderInstanceRepository.findAll();
	}

	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShAuthProviderInstanceBean shAuthProviderEdit(@PathVariable String id) {
		ShAuthProviderInstance shAuthProviderInstance = shAuthProviderInstanceRepository.findById(id).orElse(null);
		ShAuthProviderInstanceBean shAuthProviderInstanceBean = new ShAuthProviderInstanceBean();
		if (shAuthProviderInstance != null) {
			shAuthProviderInstanceBean = new ShAuthProviderInstanceBean();
			shAuthProviderInstanceBean.setId(shAuthProviderInstance.getId());
			shAuthProviderInstanceBean.setName(shAuthProviderInstance.getName());
			shAuthProviderInstanceBean.setDescription(shAuthProviderInstance.getDescription());
			shAuthProviderInstanceBean.setVendor(shAuthProviderInstance.getVendor());

			String providerInstancePath = String.format(PROVIDER_PATH, shAuthProviderInstance.getId());

			List<ShConfigVar> shConfigVars = shConfigVarRepository.findByPath(providerInstancePath);

			for (ShConfigVar shConfigVar : shConfigVars) {
				shAuthProviderInstanceBean.getProperties().put(shConfigVar.getName(), shConfigVar.getValue());
			}
		}

		return shAuthProviderInstanceBean;
	}

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShAuthProviderInstanceBean shAuthProviderInstanceAdd(@RequestBody ShAuthProviderInstanceBean shAuthProviderInstanceBean) {
		ShAuthProviderInstance shAuthProviderInstance = new ShAuthProviderInstance();

		shAuthProviderInstance.setName(shAuthProviderInstanceBean.getName());
		shAuthProviderInstance.setDescription(shAuthProviderInstanceBean.getDescription());
		shAuthProviderInstance.setVendor(shAuthProviderInstanceBean.getVendor());

		shAuthProviderInstanceRepository.save(shAuthProviderInstance);

		for (Entry<String, String> propertyEntry : shAuthProviderInstanceBean.getProperties().entrySet()) {
			String providerInstancePath = String.format(PROVIDER_PATH, shAuthProviderInstance.getId());

			ShConfigVar shConfigVar = shConfigVarRepository.findByPathAndName(providerInstancePath,
					propertyEntry.getKey());

			if (shConfigVar != null) {
				shConfigVar.setValue(propertyEntry.getValue());
			} else {
				shConfigVar = new ShConfigVar();
				shConfigVar.setPath(providerInstancePath);
				shConfigVar.setName(propertyEntry.getKey());
				shConfigVar.setValue(propertyEntry.getValue());
			}
			shConfigVarRepository.saveAndFlush(shConfigVar);
		}

		shAuthProviderInstanceBean.setId(shAuthProviderInstance.getId());

		return shAuthProviderInstanceBean;
	}

	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShAuthProviderInstanceBean shAuthProviderInstanceUpdate(@PathVariable String id,
			@RequestBody ShAuthProviderInstanceBean shAuthProviderInstanceBean) {
		Optional<ShAuthProviderInstance> shAuthProviderInstanceOptional = shAuthProviderInstanceRepository.findById(id);
		if (shAuthProviderInstanceOptional.isPresent()) {
			ShAuthProviderInstance shAuthProviderInstanceEdit = shAuthProviderInstanceOptional.get();
			shAuthProviderInstanceEdit.setName(shAuthProviderInstanceBean.getName());
			shAuthProviderInstanceEdit.setDescription(shAuthProviderInstanceBean.getDescription());
			shAuthProviderInstanceEdit.setVendor(shAuthProviderInstanceBean.getVendor());

			shAuthProviderInstanceRepository.save(shAuthProviderInstanceEdit);

			for (Entry<String, String> propertyEntry : shAuthProviderInstanceBean.getProperties().entrySet()) {
				String providerInstancePath = String.format(PROVIDER_PATH, shAuthProviderInstanceEdit.getId());

				ShConfigVar shConfigVar = shConfigVarRepository.findByPathAndName(providerInstancePath,
						propertyEntry.getKey());

				if (shConfigVar != null) {
					shConfigVar.setValue(propertyEntry.getValue());
				} else {
					shConfigVar = new ShConfigVar();
					shConfigVar.setPath(providerInstancePath);
					shConfigVar.setName(propertyEntry.getKey());
					shConfigVar.setValue(propertyEntry.getKey());
				}
				shConfigVarRepository.saveAndFlush(shConfigVar);
			}

			return shAuthProviderInstanceBean;
		}

		return null;

	}

	@DeleteMapping("/{id}")
	@Transactional
	public boolean shAuthProviderInstanceDelete(@PathVariable String id) {
		Optional<ShAuthProviderInstance> shAuthProviderInstance = shAuthProviderInstanceRepository.findById(id);
		if (shAuthProviderInstance.isPresent()) {
			String providerInstancePath = String.format(PROVIDER_PATH, id);
			shConfigVarRepository.deleteByPath(providerInstancePath);
			shAuthProviderInstanceRepository.delete(id);
			return true;
		} else
			return false;
	}

	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShAuthProviderInstanceBean shAuthProviderInstanceStructure() {
		ShAuthProviderInstanceBean shAuthProviderInstanceBean = new ShAuthProviderInstanceBean();
		return shAuthProviderInstanceBean;

	}

	@ApiOperation(value = "Sort Auth Provider")
	@PutMapping("/sort")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public Map<String, Integer> shAuthProviderInstanceSort(@RequestBody Map<String, Integer> objectOrder) {

		for (Entry<String, Integer> objectOrderItem : objectOrder.entrySet()) {
			int shObjectOrder = objectOrderItem.getValue();
			String shAuthProviderId = objectOrderItem.getKey();
			Optional<ShAuthProviderInstance> shAuthProviderInstanceOptional = shAuthProviderInstanceRepository
					.findById(shAuthProviderId);
			if (shAuthProviderInstanceOptional.isPresent()) {
				ShAuthProviderInstance shAuthProviderInstance = shAuthProviderInstanceOptional.get();
				shAuthProviderInstance.setPosition(shObjectOrder);
				shAuthProviderInstanceRepository.save(shAuthProviderInstance);

			}
		}
		return objectOrder;

	}
}
