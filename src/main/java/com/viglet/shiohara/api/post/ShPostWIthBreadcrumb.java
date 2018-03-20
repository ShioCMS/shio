package com.viglet.shiohara.api.post;

import java.util.ArrayList;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;

public class ShPostWIthBreadcrumb {

	ShPost shPost;
	
	ArrayList<ShChannel> breadcrumb;
	
	ShSite shSite;

	public ShPost getShPost() {
		return shPost;
	}

	public void setShPost(ShPost shPost) {
		this.shPost = shPost;
	}

	public ArrayList<ShChannel> getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(ArrayList<ShChannel> breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public ShSite getShSite() {
		return shSite;
	}

	public void setShSite(ShSite shSite) {
		this.shSite = shSite;
	}

	
	
}
