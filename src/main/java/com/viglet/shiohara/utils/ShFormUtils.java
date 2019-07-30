/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.utils;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.api.post.ShPostAPI;
import com.viglet.shiohara.component.form.ShFormConfiguration;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.sites.ShSitesContextURL;
import com.viglet.shiohara.widget.ShSystemWidget;
import com.viglet.shiohara.widget.ShWidgetImplementation;

@Component
public class ShFormUtils {
	@Resource
	private ApplicationContext applicationContext;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostAPI shPostAPI;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShObjectRepository shObjectRepository;

	private ShFormConfiguration shFormConfiguration = null;
	private ShPostType shPostType = null;

	private ShPost createPost(ShSitesContextURL shSitesContextURL)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Enumeration<String> parameters = shSitesContextURL.getRequest().getParameterNames();
		ShPost shPost = null;
		ShObject shObject = shObjectRepository.findById(shSitesContextURL.getInfo().getObjectId()).orElse(null);
		if (shFormConfiguration != null || shObject instanceof ShFolder || (shObject instanceof ShPost
				&& ((ShPost) shObject).getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX))) {
			ShFolder shFolder = null;

			if (StringUtils.isNotBlank(shFormConfiguration.getFolder().toString())) {
				shFolder = shFolderRepository.findById(shFormConfiguration.getFolder().toString()).orElse(null);
			} else {
				if (shObject instanceof ShFolder) {
					shFolder = (ShFolder) shObject;
				} else {
					if (shObject != null)
						shFolder = ((ShPost) shObject).getShFolder();
				}
			}

			if (shPostType != null) {
				shPost = new ShPost();
				shPost.setDate(new Date());
				shPost.setOwner("anonymous");
				shPost.setShFolder(shFolder);

				shPost.setShPostType(shPostType);
				Set<ShPostAttr> shPostAttrs = new HashSet<ShPostAttr>();
				while (parameters.hasMoreElements()) {
					String param = parameters.nextElement();
					String paramValue = shSitesContextURL.getRequest().getParameter(param);

					if (param.startsWith("__sh-post-type-attr-")) {
						String attribute = param.replaceFirst("__sh-post-type-attr-", "");

						ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType,
								attribute);

						String className = shPostTypeAttr.getShWidget().getClassName();
						ShWidgetImplementation object = (ShWidgetImplementation) Class.forName(className).newInstance();
						applicationContext.getAutowireCapableBeanFactory().autowireBean(object);

						@SuppressWarnings("unused")
						boolean attrStatus = object.validateForm(shSitesContextURL.getRequest(), shPostTypeAttr);
						// TODO: Create validation Form logic

						ShPostAttr shPostAttr = new ShPostAttr();
						shPostAttr.setShPost(shPost);
						shPostAttr.setShPostTypeAttr(shPostTypeAttr);
						shPostAttr.setStrValue(paramValue);

						shPostAttrs.add(shPostAttr);
					}

				}
				shPost.setShPostAttrs(shPostAttrs);

				shPostAPI.postSave(shPost);

			}
		}

		return shPost;
	}

	public void execute(ShSitesContextURL shSitesContextURL)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

		shPostType = shPostTypeRepository.findByName(shSitesContextURL.getRequest().getParameter("__sh-post-type"));
		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.FORM_CONFIGURATION)) {
				JSONObject formConfiguration = new JSONObject(shPostTypeAttr.getWidgetSettings());
				shFormConfiguration = new ShFormConfiguration(formConfiguration);
			}
		}
		ShPost shPost = this.createPost(shSitesContextURL);

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			String className = shPostTypeAttr.getShWidget().getClassName();
			ShWidgetImplementation object = (ShWidgetImplementation) Class.forName(className).newInstance();
			applicationContext.getAutowireCapableBeanFactory().autowireBean(object);
			object.postRender(shPost, shSitesContextURL);

		}

		if (!shFormConfiguration.isCreatePost()) {
			shPostRepository.delete(shPost);
		}
	}
}
