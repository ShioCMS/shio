package com.viglet.shiohara.api.folder;

import java.util.ArrayList;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.site.ShSite;

public class ShFolderPath {

	String folderPath;
	
	ShFolder currentFolder;
	
	ArrayList<ShFolder> breadcrumb;
	
	ShSite shSite;

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public ShFolder getCurrentFolder() {
		return currentFolder;
	}

	public void setCurrentFolder(ShFolder currentFolder) {
		this.currentFolder = currentFolder;
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
