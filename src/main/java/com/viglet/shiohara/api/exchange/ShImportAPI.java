package com.viglet.shiohara.api.exchange;

import java.io.IOException;
import java.security.Principal;

import org.apache.commons.compress.archivers.ArchiveException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.exchange.ShImportExchange;
import com.viglet.shiohara.exchange.ShExchange;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/import")
@Api(tags = "Import", description = "Import objects into Viglet Shiohara")
public class ShImportAPI {

	@Autowired
	private ShImportExchange shImportExchange;

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	@Transactional
	public ShExchange shImport(@RequestParam("file") MultipartFile multipartFile, final Principal principal)
			throws IllegalStateException, IOException, ArchiveException {
		return shImportExchange.importFromMultipartFile(multipartFile, principal.getName());
	}

}
