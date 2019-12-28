package com.viglet.shiohara.provider.exchange;

import java.io.InputStream;
import java.util.Map;

public interface ShExchangeProvider {

	public void init(Map<String, String> variables);

	public ShExchangeProviderFolder getRootFolder();

	public ShExchangeProviderPost getObject(String id, boolean isFolder);

	public ShExchangeProviderFolder getFolder(String id);

	public InputStream getDownload(String id);

}
