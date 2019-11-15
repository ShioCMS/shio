/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.sites;

import java.io.Serializable;

public class ShSitesContextURLInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String shContext = null;
	private String contextURL = null;
	private String contextURLOriginal = null;
	private String siteId = null;
	private String parentFolderId = null;
	private String shFormat = null;
	private String shLocale = null;
	private String objectId = null;
	private boolean cacheEnabled = true;
	private boolean staticFile = false;
	private boolean pageAllowGuestUser = true;
	private boolean pageAllowRegisterUser = true;

	public String toString() {
		return String.format(
				"shContext: %s, contextURL: %s, shContextOriginal: %s, shSite: %s, shParentFolder: %s, shFormat: %s, shLocale: %s, shObject:  %s",
				shContext, contextURL, contextURLOriginal, siteId, parentFolderId, shFormat, shLocale, objectId);
	}

	public String getShContext() {
		return shContext;
	}

	public void setShContext(String shContext) {
		this.shContext = shContext;
	}

	public String getContextURL() {
		return contextURL;
	}

	public void setContextURL(String contextURL) {
		this.contextURL = contextURL;
	}

	public String getContextURLOriginal() {
		return contextURLOriginal;
	}

	public void setContextURLOriginal(String contextURLOriginal) {
		this.contextURLOriginal = contextURLOriginal;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public String getShFormat() {
		return shFormat;
	}

	public void setShFormat(String shFormat) {
		this.shFormat = shFormat;
	}

	public String getShLocale() {
		return shLocale;
	}

	public void setShLocale(String shLocale) {
		this.shLocale = shLocale;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}

	public boolean isStaticFile() {
		return staticFile;
	}

	public void setStaticFile(boolean staticFile) {
		this.staticFile = staticFile;
	}

	public boolean isPageAllowGuestUser() {
		return pageAllowGuestUser;
	}

	public void setPageAllowGuestUser(boolean pageAllowGuestUser) {
		this.pageAllowGuestUser = pageAllowGuestUser;
	}

	public boolean isPageAllowRegisterUser() {
		return pageAllowRegisterUser;
	}

	public void setPageAllowRegisterUser(boolean pageAllowRegisterUser) {
		this.pageAllowRegisterUser = pageAllowRegisterUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
