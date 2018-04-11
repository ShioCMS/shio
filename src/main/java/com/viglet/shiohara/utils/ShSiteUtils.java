package com.viglet.shiohara.utils;

import java.util.UUID;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.post.type.ShSystemPostType;

@Component
public class ShSiteUtils {
	public JSONObject toJSON(ShSite shSite, String shContext) {
		JSONObject shSiteItemSystemAttrs = new JSONObject();
		shSiteItemSystemAttrs.put("id", shSite.getId());
		shSiteItemSystemAttrs.put("title", shSite.getName());
		shSiteItemSystemAttrs.put("summary", shSite.getDescription());
		shSiteItemSystemAttrs.put("link",
				"/" + shContext + "/" + shSite.getName().replaceAll(" ", "-") + "/default/pt-br");

		JSONObject shSiteItemAttrs = new JSONObject();

		shSiteItemAttrs.put("system", shSiteItemSystemAttrs);
		return shSiteItemAttrs;
	}
	
	public String generatePostLink(ShSite shSite) {	
		String shContext = "sites";
		String link = "/" + shContext + "/";
		link = link + shSite.getName().replaceAll(" ", "-");
		link = link + "/default/pt-br";
		link = link + "/Home";
		return link;
	}

}
