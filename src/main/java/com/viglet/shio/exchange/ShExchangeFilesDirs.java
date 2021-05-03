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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Exchange Files and Directories.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 * 
 */
public class ShExchangeFilesDirs {

	private static final Log logger = LogFactory.getLog(ShExchangeFilesDirs.class);

	private static final String EXPORT_FILE = "export.json";

	private File exportDir;
	private File tmpDir;
	private File exportJsonFile;
	private File zipFile;
	private File parentExportDir;

	public boolean generate() {
		return generate(null);
	}

	public boolean generate(File exportDirCustom) {
		String folderName = UUID.randomUUID().toString();
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			createTmpDir(userDir);
			createExportDir(exportDirCustom, folderName);
		}

		this.zipFile = new File(tmpDir.getAbsolutePath().concat(File.separator + folderName + ".zip"));
		this.updateExportJsonFilePath();
		return this.exportDir.exists();
	}

	private void createTmpDir(File userDir) {
		this.tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
		if (!this.tmpDir.exists())
			this.tmpDir.mkdirs();
	}

	private void createExportDir(File exportDirCustom, String folderName) {
		this.exportDir = (exportDirCustom != null) ? exportDirCustom
				: new File(tmpDir.getAbsolutePath().concat(File.separator + folderName));
		if (!this.exportDir.exists())
			this.exportDir.mkdirs();
	}

	public void deleteExport() {

		try {
			if (this.parentExportDir != null)
				FileUtils.deleteDirectory(this.parentExportDir);
			else
				FileUtils.deleteDirectory(this.exportDir);
			FileUtils.deleteQuietly(this.zipFile);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public File updateExportJsonFilePath() {
		this.exportJsonFile = new File(this.exportDir.getAbsolutePath().concat(File.separator + EXPORT_FILE));
		return this.exportJsonFile;
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

	public File getParentExportDir() {
		return parentExportDir;
	}

	public void setParentExportDir(File parentExportDir) {
		this.parentExportDir = parentExportDir;
	}

	public ShExchange readExportFile() {
		ShExchange shExchange = null;
		if (this.getExportDir() != null) {
			// Check if export.json exists, if it is not exist try access a sub directory
			if (!(new File(this.getExportDir(), EXPORT_FILE).exists())
					&& (this.getExportDir().listFiles().length == 1)) {
				for (File fileOrDirectory : this.getExportDir().listFiles()) {
					if (fileOrDirectory.isDirectory() && new File(fileOrDirectory, EXPORT_FILE).exists()) {
						this.parentExportDir = this.getExportDir();
						this.setExportDir(fileOrDirectory);
						this.updateExportJsonFilePath();
					}
				}
			}
			ObjectMapper mapper = new ObjectMapper();

			try {
				shExchange = mapper.readValue(new FileInputStream(exportJsonFile), ShExchange.class);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return shExchange;
	}
}
