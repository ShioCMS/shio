package com.viglet.shiohara.provider.otmm.bean.folders;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMFoldersResourceBean {

	@JsonProperty("folder_list")
	private List<ShOTMMFolderBean> folderList;

	public List<ShOTMMFolderBean> getFolderList() {
		return folderList;
	}

	public void setFolderList(List<ShOTMMFolderBean> folderList) {
		this.folderList = folderList;
	}

	

}
