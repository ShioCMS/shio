package com.viglet.shiohara.api.staticfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.utils.ShStaticFileUtils;

@Component
@Path("/staticfile")
public class ShStaticFileAPI {

	@Autowired
	private ShChannelRepository shChannelRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	@Path("upload")
	public String fileUpload(@DefaultValue("true") @FormDataParam("enabled") boolean enabled,
			@FormDataParam("file") InputStream inputStream, @FormDataParam("channelId") UUID channelId,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @Context UriInfo uriInfo)
			throws URISyntaxException {

		ShChannel shChannel = shChannelRepository.findById(channelId);
		File directoryPath = shStaticFileUtils.dirPath(shChannel);
		String filePath = null;
		if (directoryPath != null) {
			if (!directoryPath.exists()) {
				directoryPath.mkdirs();
			}
			OutputStream outputStream = null;

			try {

				// write the inputStream to a FileOutputStream
				String destFile = directoryPath.getAbsolutePath().concat("/" + fileDetail.getFileName());

				outputStream = new FileOutputStream(new File(destFile));

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}

				filePath = fileDetail.getFileName();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (outputStream != null) {
					try {
						// outputStream.flush();
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}
		return "{ \"file\":\"" + filePath + "\"}";
	}

}
