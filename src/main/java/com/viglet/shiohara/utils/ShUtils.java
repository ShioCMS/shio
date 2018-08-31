package com.viglet.shiohara.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ShUtils {
	/**
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param outputFolder
	 *            output Folder
	 * @throws ArchiveException
	 *             if creating or adding to the archive fails
	 * @throws IOException
	 *             if the io fails
	 */
	public void unZipIt(File zipFile, File outputFolder) throws ArchiveException, IOException {

		try (ZipArchiveInputStream zin = new ZipArchiveInputStream(new FileInputStream(zipFile))) {
			ZipArchiveEntry entry;
			while ((entry = zin.getNextZipEntry()) != null) {
				if (entry.isDirectory()) {
					continue;
				}
				File curfile = new File(outputFolder, entry.getName());
				File parent = curfile.getParentFile();
				if (!parent.exists()) {
					if (!parent.mkdirs()) {
						throw new RuntimeException("could not create directory: " + parent.getPath());
					}
				}
				IOUtils.copy(zin, new FileOutputStream(curfile));
			}
		}
	}

	/**
	 * Add all files from the source directory to the destination zip file
	 *
	 * @param source
	 *            the directory with files to add
	 * @param destination
	 *            the zip file that should contain the files
	 * @throws IOException
	 *             if the io fails
	 * @throws ArchiveException
	 *             if creating or adding to the archive fails
	 */
	public void addFilesToZip(File source, File destination) throws IOException, ArchiveException {
		OutputStream archiveStream = new FileOutputStream(destination);
		ArchiveOutputStream archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP,
				archiveStream);

		Collection<File> fileList = FileUtils.listFiles(source, null, true);

		for (File file : fileList) {
			String entryName = getEntryName(source, file);
			ZipArchiveEntry entry = new ZipArchiveEntry(entryName);
			archive.putArchiveEntry(entry);

			BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));

			IOUtils.copy(input, archive);
			input.close();
			archive.closeArchiveEntry();
		}

		archive.finish();
		archiveStream.close();
	}

	/**
	 * Remove the leading part of each entry that contains the source directory name
	 *
	 * @param source
	 *            the directory where the file entry is found
	 * @param file
	 *            the file that is about to be added
	 * @return the name of an archive entry
	 * @throws IOException
	 *             if the io fails
	 */
	private String getEntryName(File source, File file) throws IOException {
		int index = source.getAbsolutePath().length() + 1;
		String path = file.getCanonicalPath();

		return path.substring(index);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String asJsonStringAndView(final Object obj, @SuppressWarnings("rawtypes") Class clazz) {
		try {
			return new ObjectMapper().writerWithView(clazz).writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isJSONValid(String test) {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}
}
