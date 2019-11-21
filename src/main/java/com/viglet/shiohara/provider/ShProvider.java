package com.viglet.shiohara.provider;

import java.io.InputStream;

public interface ShProvider {

	public void init(String baseURL, String username, String password);

	public ShProviderFolder getRootFolder();

	public ShProviderPost getObject(String id);

	public ShProviderFolder getFolder(String id);

	public InputStream getDownload(String id);

}
