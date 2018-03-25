package com.viglet.shiohara.api.folder;

import java.util.ArrayList;
import java.util.List;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;

public class ShFolderList {

	List<ShFolder> shFolders;

	List<ShPost> shPosts;
	
	String folderPath;
	
	ArrayList<ShFolder> breadcrumb;
	
	ShSite shSite;
	
	public List<ShFolder> getShFolders() {
		return shFolders;
	}

	public void setShFolders(List<ShFolder> shFolders) {
		this.shFolders = shFolders;
	}

	public List<ShPost> getShPosts() {
		return shPosts;
	}

	public void setShPosts(List<ShPost> shPosts) {
		this.shPosts = shPosts;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public ArrayList<ShFolder> getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(ArrayList<ShFolder> breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public ShSite getShSite() {
		return shSite;
	}

	public void setShSite(ShSite shSite) {
		this.shSite = shSite;
	}

}
