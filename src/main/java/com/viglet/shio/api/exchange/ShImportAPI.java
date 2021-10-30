/*
 * Copyright (C) 2016-2020 the original author or authors. 
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
package com.viglet.shio.api.exchange;

import java.security.Principal;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shio.api.ShJsonView;
import com.viglet.shio.exchange.ShImportExchange;
import com.viglet.shio.provider.exchange.blogger.ShExchangeBloggerImport;
import com.viglet.shio.exchange.ShExchange;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Alexandre Oliveira
 */
@RestController
@RequestMapping("/api/v2/import")
@Tag(name = "Import", description = "Import objects into Viglet Shio CMS")
public class ShImportAPI {
	@Autowired
	private ShImportExchange shImportExchange;
	@Autowired
	private ShExchangeBloggerImport shExchangeBloggerImport;

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShExchange shImport(@RequestParam("file") @Nonnull MultipartFile multipartFile, final Principal principal) {

		if (multipartFile.getOriginalFilename() != null) {
			String fileName = multipartFile.getOriginalFilename();
			if (fileName.endsWith("xml")) {
				return shExchangeBloggerImport.shImportFromBlogger(multipartFile);
			} else {
				return shImportExchange.importFromMultipartFile(multipartFile);
			}
		}
		return new ShExchange();
	}

}
