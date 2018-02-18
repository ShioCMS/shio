package com.viglet.shiohara.utils;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;

@Component
public class ShStaticFileUtils {
	@Autowired
	ShChannelRepository shChannelRepository;
	@Autowired
	ShChannelUtils shChannelUtils;
	String fileSourceBase = File.separator + "store" + File.separator +"file_source";

	public File dirPath(ShChannel shChannel) {
		File directoryPath = null;
		ShSite shSite = shChannelUtils.getSite(shChannel);
		String channelPath = shChannelUtils.channelPath(shChannel, File.separator);
		String channelPathFile = fileSourceBase.concat(File.separator + shSite.getName() + channelPath);
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			directoryPath = new File(userDir.getAbsolutePath().concat(channelPathFile));

		}
		return directoryPath;
	}

	public File filePath(ShChannel shChannel, String fileName) {
		File file = null;
		File directoryPath = this.dirPath(shChannel);

		if (directoryPath != null) {
			file = new File(directoryPath.getAbsolutePath().concat(File.separator + fileName));

		}
		return file;
	}

	public File filePath(String url) {
		File file = null;
		File userDir = new File(System.getProperty("user.dir"));
		String fileRelativePath = url.replaceAll("/", File.separator);

		if (userDir.exists() && userDir.isDirectory()) {
			String fileSource = userDir.getAbsolutePath().concat(fileSourceBase);
			file = new File(fileSource.concat(fileRelativePath));

		}
		return file;
	}
	

	public File getFileSource() {
		File file = null;
		File userDir = new File(System.getProperty("user.dir"));

		if (userDir.exists() && userDir.isDirectory()) {
			String fileSource = userDir.getAbsolutePath().concat(fileSourceBase);
			file = new File(fileSource);

		}
		return file;
	}
}
