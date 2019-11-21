package com.viglet.shiohara.api.provider;

import java.io.IOException;
import java.io.InputStream;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.provider.ShProvider;
import com.viglet.shiohara.provider.ShProviderFolder;
import com.viglet.shiohara.provider.ShProviderPost;
import com.viglet.shiohara.utils.ShStaticFileUtils;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/provider")
@Api(tags = "Provider", description = "Provider API")
public class ShProviderAPI {
	private static final Log logger = LogFactory.getLog(ShProviderAPI.class);
	private ShProvider shProvider;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;

	private void initProvider() {
		try {
			shProvider = (ShProvider) Class.forName("com.viglet.shiohara.provider.otcs.ShOTCSProvider").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			logger.error("initProvider: ", e);
		}
		shProvider.init("http://localhost/OTCS/cs.exe", "admin", "liberty09!");
	}

	@PostMapping("/{providerId}/import/{providerItemId}/to/{folderId}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shProviderImportItem(@PathVariable String folderId, @PathVariable String providerId,
			@PathVariable String providerItemId) {

		this.initProvider();

		ShProviderPost shProviderPost = shProvider.getObject(providerItemId);
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

	@GetMapping("/{id}/list")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShProviderFolder shObjectListItem(@PathVariable String id) {
		this.initProvider();

		ShProviderFolder shProviderFolder = shProvider.getFolder(id);

		return shProviderFolder;
	}
}
