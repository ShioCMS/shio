/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shio.provider.exchange;

import java.io.InputStream;
import java.util.Map;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
public interface ShExchangeProvider {

	public void init(Map<String, String> variables);

	public ShExchangeProviderFolder getRootFolder();

	public ShExchangeProviderPost getObject(String id, boolean isFolder);

	public ShExchangeProviderFolder getFolder(String id);

	public InputStream getDownload(String id);

}
