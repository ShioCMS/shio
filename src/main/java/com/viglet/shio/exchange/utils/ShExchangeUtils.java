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
package com.viglet.shio.exchange.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shio.exchange.ShExchange;
import com.viglet.shio.exchange.ShExchangeFilesDirs;
import com.viglet.shio.utils.ShUtils;

/**
 * Exchange Utils.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 * 
 */
@Component
public class ShExchangeUtils {
	private static final Log logger = LogFactory.getLog(ShExchangeUtils.class);
	@Autowired
	private ShUtils shUtils;
	public File responseWithZipFile(String suffixFileName, HttpServletResponse response, ShExchange shExchange,
			ShExchangeFilesDirs shExchangeFilesDirs) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(shExchangeFilesDirs.getExportJsonFile(), shExchange);
		} catch (IOException e) {
			logger.error(e);
		}
		
		shUtils.addFilesToZip(shExchangeFilesDirs.getExportDir(), shExchangeFilesDirs.getZipFile());

		String strDate = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
		String zipFileName = String.format("%s_%s.zip", suffixFileName, strDate);

		response.addHeader("Content-disposition", "attachment;filename=" + zipFileName);
		response.setContentType("application/octet-stream");
		response.setStatus(HttpServletResponse.SC_OK);
		return shExchangeFilesDirs.getZipFile();
	}

	public StreamingResponseBody responseBodyFromZipFle(ShExchangeFilesDirs shExchangeFilesDirs) {
		return new StreamingResponseBody() {
			@Override
			public void writeTo(java.io.OutputStream output) throws IOException {

				try {
					java.nio.file.Path path = Paths.get(shExchangeFilesDirs.getZipFile().getAbsolutePath());
					byte[] data = Files.readAllBytes(path);
					output.write(data);
					output.flush();

					shExchangeFilesDirs.deleteExport();

				} catch (IOException ex) {
					logger.error(ex);
				}
			}
		};
	}

	public StreamingResponseBody downloadZipFile(String suffixName, HttpServletResponse response, ShExchange shExchange,
			ShExchangeFilesDirs shExchangeFilesDirs) {
		this.responseWithZipFile(suffixName, response, shExchange, shExchangeFilesDirs);

		return this.responseBodyFromZipFle(shExchangeFilesDirs);
	}

}
