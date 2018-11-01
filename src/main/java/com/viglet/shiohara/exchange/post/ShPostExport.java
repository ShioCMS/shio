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

package com.viglet.shiohara.exchange.post;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.exchange.ShFileExchange;
import com.viglet.shiohara.exchange.ShRelatorExchange;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.relator.ShRelatorItem;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.utils.ShStaticFileUtils;
import com.viglet.shiohara.widget.ShSystemWidget;

@Component
public class ShPostExport {
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;

	public void shPostAttrExchangeIterate(ShPost shPost, Set<ShPostAttr> shPostAttrs, Map<String, Object> fields,
			List<ShFileExchange> files) {
		for (ShPostAttr shPostAttr : shPostAttrs) {
			if (shPostAttr != null && shPostAttr.getShPostTypeAttr() != null) {
				if (shPostAttr.getShPostTypeAttr().getShWidget().getName().equals(ShSystemWidget.RELATOR)) {
					ShRelatorExchange shRelatorExchange = new ShRelatorExchange();
					shRelatorExchange.setId(shPostAttr.getId());
					shRelatorExchange.setName(shPostAttr.getStrValue());
					Set<Object> relators = new HashSet<Object>();
					for (ShRelatorItem shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
						Map<String, Object> relatorFields = new HashMap<String, Object>();
						this.shPostAttrExchangeIterate(shPost, shRelatorItem.getShChildrenPostAttrs(), relatorFields,
								files);
						relators.add(relatorFields);
					}
					shRelatorExchange.setShSubPosts(relators);
					fields.put(shPostAttr.getShPostTypeAttr().getName(), shRelatorExchange);
				} else {
					fields.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
				}

				if (shPostAttr.getShPostTypeAttr().getName().equals(ShSystemPostTypeAttr.FILE)
						&& shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
					String fileName = shPostAttr.getStrValue();
					File directoryPath = shStaticFileUtils.dirPath(shPost.getShFolder());
					File file = new File(directoryPath.getAbsolutePath().concat(File.separator + fileName));
					ShFileExchange shFileExchange = new ShFileExchange();
					shFileExchange.setId(shPost.getId());
					shFileExchange.setFile(file);
					files.add(shFileExchange);
				}
			}
		}
	}
}
