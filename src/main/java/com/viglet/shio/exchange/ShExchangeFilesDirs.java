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
package com.viglet.shio.exchange;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Exchange Files and Directories.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 * 
 */
public class ShExchangeFilesDirs {

	private static final Log logger = LogFactory.getLog(ShExchangeFilesDirs.class);
	
	private File exportDir;
	private File tmpDir;
	private File exportJsonFile;
	private File zipFile;
	
	public boolean generate() {
		String folderName = UUID.randomUUID().toString();
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			this.tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!this.tmpDir.exists())
				this.tmpDir.mkdirs();
			this.exportDir = new File(tmpDir.getAbsolutePath().concat(File.separator + folderName));
			if (!this.exportDir.exists())
				this.exportDir.mkdirs();
		}

		this.zipFile = new File(tmpDir.getAbsolutePath().concat(File.separator + folderName + ".zip"));
		this.exportJsonFile = new File(this.exportDir.getAbsolutePath().concat(File.separator + "export.json"));
		return this.exportDir.exists();
	}

	public void deleteExport() {

		try {
			FileUtils.deleteDirectory(this.exportDir);
			FileUtils.deleteQuietly(this.zipFile);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public File getExportDir() {
		return exportDir;
	}

	public void setExportDir(File exportDir) {
		this.exportDir = exportDir;
	}

	public File getTmpDir() {
		return tmpDir;
	}

	public void setTmpDir(File tmpDir) {
		this.tmpDir = tmpDir;
	}

	public File getZipFile() {
		return zipFile;
	}

	public void setZipFile(File zipFile) {
		this.zipFile = zipFile;
	}

	public File getExportJsonFile() {
		return exportJsonFile;
	}

	public void setExportJsonFile(File exportJsonFile) {
		this.exportJsonFile = exportJsonFile;
	}

}
