package com.viglet.shiohara.provider;

import java.io.InputStream;
import java.util.Map;

public interface ShProvider {

	public void init(Map<String, String> variables);

	public ShProviderFolder getRootFolder();

	public ShProviderPost getObject(String id);

	public ShProviderFolder getFolder(String id);

	public InputStream getDownload(String id);

}
