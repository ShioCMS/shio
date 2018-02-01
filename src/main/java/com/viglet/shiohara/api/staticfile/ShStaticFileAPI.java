package com.viglet.shiohara.api.staticfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
@Path("/staticfile")
public class ShStaticFileAPI {

	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShChannelRepository shChannelRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	@Path("upload")
	public Response fileUpload(@DefaultValue("true") @FormDataParam("enabled") boolean enabled,
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @Context UriInfo uriInfo)
			throws URISyntaxException {

		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File fileSourceDir = new File(userDir.getAbsolutePath().concat("/store/file_source"));
			if (!fileSourceDir.exists()) {
				fileSourceDir.mkdirs();
			}

			OutputStream outputStream = null;

			try {

				// write the inputStream to a FileOutputStream
				String destFile = fileSourceDir.getAbsolutePath().concat("/" + fileDetail.getFileName());

				outputStream = new FileOutputStream(new File(destFile));

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}

				// Post File
				ShSite shSite = shSiteRepository.findById(1);
				ShPostType shPostType = shPostTypeRepository.findByName("PT-FILE");
				ShChannel shChannelHome = shChannelRepository.findByShSiteAndName(shSite, "Home");

				ShPost shPost = new ShPost();
				shPost.setDate(new Date());
				shPost.setShPostType(shPostType);
				shPost.setSummary(null);
				shPost.setTitle(fileDetail.getFileName());
				shPost.setShChannel(shChannelHome);

				shPostRepository.save(shPost);

				ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType, "File");

				ShPostAttr shPostAttr = new ShPostAttr();
				shPostAttr.setShPost(shPost);
				shPostAttr.setShPostType(shPostType);
				shPostAttr.setShPostTypeAttr(shPostTypeAttr);
				shPostAttr.setShPostTypeAttrId(1);
				shPostAttr.setStrValue(shPost.getTitle());
				shPostAttr.setType(1);

				shPostAttrRepository.save(shPostAttr);

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
		return Response.ok().build();
	}

}
