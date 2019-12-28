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

package com.viglet.shiohara.api.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.bean.provider.ShProviderInstanceBean;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.provider.ShProviderInstance;
import com.viglet.shiohara.persistence.model.provider.ShProviderVendor;
import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.provider.ShProviderInstanceRepository;
import com.viglet.shiohara.persistence.repository.provider.ShProviderVendorRepository;
import com.viglet.shiohara.persistence.repository.system.ShConfigVarRepository;
import com.viglet.shiohara.provider.ShProvider;
import com.viglet.shiohara.provider.ShProviderFolder;
import com.viglet.shiohara.provider.ShProviderPost;
import com.viglet.shiohara.utils.ShConfigVarUtils;
import com.viglet.shiohara.utils.ShStaticFileUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v2/provider")
@Api(tags = "Provider", description = "Provider API")
public class ShProviderAPI {
	private static final Log logger = LogFactory.getLog(ShProviderAPI.class);

	private static final String PROVIDER_PATH = "/provider/%s";

	private ShProvider shProvider;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShConfigVarUtils shConfigVarUtils;
	@Autowired
	private ShProviderInstanceRepository shProviderInstanceRepository;
	@Autowired
	private ShProviderVendorRepository shProviderVendorRepository;
	@Autowired
	private ShConfigVarRepository shConfigVarRepository;

	@GetMapping("/vendor")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShProviderVendor> shProviderVendorListItem() {
		return shProviderVendorRepository.findAll();
	}

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShProviderInstance> shProviderInstanceListItem() {
		return shProviderInstanceRepository.findAll();
	}

	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShProviderInstanceBean shProviderEdit(@PathVariable String id) {
		ShProviderInstance shProviderInstance = shProviderInstanceRepository.findById(id).orElse(null);
		ShProviderInstanceBean shProviderInstanceBean = new ShProviderInstanceBean();
		if (shProviderInstance != null) {
			shProviderInstanceBean = new ShProviderInstanceBean();
			shProviderInstanceBean.setId(shProviderInstance.getId());
			shProviderInstanceBean.setName(shProviderInstance.getName());
			shProviderInstanceBean.setDescription(shProviderInstance.getDescription());
			shProviderInstanceBean.setVendor(shProviderInstance.getVendor());

			String providerInstancePath = String.format(PROVIDER_PATH, shProviderInstance.getId());

			List<ShConfigVar> shConfigVars = shConfigVarRepository.findByPath(providerInstancePath);

			for (ShConfigVar shConfigVar : shConfigVars) {
				shProviderInstanceBean.getProperties().put(shConfigVar.getName(), shConfigVar.getValue());
			}
		}

		return shProviderInstanceBean;
	}

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShProviderInstanceBean shProviderInstanceAdd(@RequestBody ShProviderInstanceBean shProviderInstanceBean) {
		ShProviderInstance shProviderInstance = new ShProviderInstance();

		shProviderInstance.setName(shProviderInstanceBean.getName());
		shProviderInstance.setDescription(shProviderInstanceBean.getDescription());
		shProviderInstance.setVendor(shProviderInstanceBean.getVendor());

		shProviderInstanceRepository.save(shProviderInstance);

		for (Entry<String, String> propertyEntry : shProviderInstanceBean.getProperties().entrySet()) {
			String providerInstancePath = String.format(PROVIDER_PATH, shProviderInstance.getId());

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

		shProviderInstanceBean.setId(shProviderInstance.getId());

		return shProviderInstanceBean;
	}

	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShProviderInstanceBean shProviderInstanceUpdate(@PathVariable String id,
			@RequestBody ShProviderInstanceBean shProviderInstanceBean) {
		Optional<ShProviderInstance> shProviderInstanceOptional = shProviderInstanceRepository.findById(id);
		if (shProviderInstanceOptional.isPresent()) {
			ShProviderInstance shProviderInstanceEdit = shProviderInstanceOptional.get();
			shProviderInstanceEdit.setName(shProviderInstanceBean.getName());
			shProviderInstanceEdit.setDescription(shProviderInstanceBean.getDescription());
			shProviderInstanceEdit.setVendor(shProviderInstanceBean.getVendor());

			shProviderInstanceRepository.save(shProviderInstanceEdit);

			for (Entry<String, String> propertyEntry : shProviderInstanceBean.getProperties().entrySet()) {
				String providerInstancePath = String.format(PROVIDER_PATH, shProviderInstanceEdit.getId());

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

			return shProviderInstanceBean;
		}

		return null;

	}

	@DeleteMapping("/{id}")
	@Transactional
	public boolean shProviderInstanceDelete(@PathVariable String id) {
		Optional<ShProviderInstance> shProviderInstance = shProviderInstanceRepository.findById(id);
		if (shProviderInstance.isPresent()) {
			String providerInstancePath = String.format(PROVIDER_PATH, id);
			shConfigVarRepository.deleteByPath(providerInstancePath);
			shProviderInstanceRepository.delete(id);
			return true;
		} else
			return false;
	}

	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShProviderInstanceBean shProviderInstanceStructure() {
		ShProviderInstanceBean shProviderInstanceBean = new ShProviderInstanceBean();
		return shProviderInstanceBean;

	}

	@ApiOperation(value = "Sort Exchange Provider")
	@PutMapping("/sort")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public Map<String, Integer> shProviderInstanceSort(@RequestBody Map<String, Integer> objectOrder) {

		for (Entry<String, Integer> objectOrderItem : objectOrder.entrySet()) {
			int shObjectOrder = objectOrderItem.getValue();
			String shExchangeProviderId = objectOrderItem.getKey();
			Optional<ShProviderInstance> shProviderInstanceOptional = shProviderInstanceRepository
					.findById(shExchangeProviderId);
			if (shProviderInstanceOptional.isPresent()) {
				ShProviderInstance shProviderInstance = shProviderInstanceOptional.get();
				shProviderInstance.setPosition(shObjectOrder);
				shProviderInstanceRepository.save(shProviderInstance);

			}
		}
		return objectOrder;

	}

	private void initProvider(String providerInstanceId) {
		ShProviderInstance shProviderInstance = shProviderInstanceRepository.findById(providerInstanceId).orElse(null);
		if (shProviderInstance != null) {
			Map<String, String> variables = shConfigVarUtils
					.getVariablesFromPath(String.format("/provider/%s", providerInstanceId));

			try {
				shProvider = (ShProvider) Class.forName(shProviderInstance.getVendor().getClassName()).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				logger.error("initProvider: ", e);
			}
			shProvider.init(variables);
		}
	}

	@PostMapping("/{providerInstanceId}/import/{providerItemId}/to/{folderId}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shProviderImportItem(@PathVariable String folderId, @PathVariable String providerInstanceId,
			@PathVariable String providerItemId) {

		this.initProvider(providerInstanceId);

		ShProviderPost shProviderPost = shProvider.getObject(providerItemId, false);

		String fileName = shProviderPost.getTitle();
		InputStream is = shProvider.getDownload(providerItemId);

		try {

			MultipartFile file = new MockMultipartFile(fileName, IOUtils.toByteArray(is));

			ShFolder shFolder = shFolderRepository.findById(folderId).orElse(null);
			if (shFolder != null) {
				TikaConfig config = TikaConfig.getDefaultConfig();
				String mediaType = new Tika().detect(file.getInputStream());
				MimeType mimeType = config.getMimeRepository().forName(mediaType);
				String extension = mimeType.getExtension();
				if (!StringUtils.isEmpty(extension)) {
					String fileWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
					String fileNameFormatted = String.format("%s%s", fileWithoutExtension, extension);
					fileName = fileNameFormatted;
				}
				return shStaticFileUtils.createFilePost(file, fileName, shFolder, true);
			}
		} catch (IOException e) {
			logger.error("shProviderImportItemIOException", e);
		} catch (MimeTypeException e) {
			logger.error("shProviderImportItemMimeTypeException", e);
		}
		return null;
	}

	@GetMapping("/{providerInstanceId}/{id}/list")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShProviderFolder shProviderListItem(@PathVariable String providerInstanceId, @PathVariable String id) {
		this.initProvider(providerInstanceId);
		if (id.equals("_root"))
			return shProvider.getRootFolder();
		else
			return shProvider.getFolder(id);

	}
}
