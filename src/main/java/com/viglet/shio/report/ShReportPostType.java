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
package com.viglet.shio.report;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.bean.IShPostTypeCount;
import com.viglet.shio.bean.ShPostTypeReport;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.folder.ShFolderRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.persistence.repository.site.ShSiteRepository;

/**
 * Post Type Report
 * 
 * @author Alexandre Oliveira
 * @since 0.3.7
 * 
 */
@Component
public class ShReportPostType {
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostRepository shPostRepository;

	public List<ShPostTypeReport> postTypeCountBySite(String id) {
		ShSite shSite = shSiteRepository.findById(id).orElse(null);
		List<IShPostTypeCount> shPostTypeCounts = shPostRepository.counShPostTypeByShSite(shSite);
		int countFolder = shFolderRepository.countByShSite(shSite);

		Map<String, Float> countTypes = new HashMap<>();

		countTypes.put("Folder", (float) countFolder);

		long total = countFolder;

		for (IShPostTypeCount postTypeCount : shPostTypeCounts) {
			countTypes.put(postTypeCount.getShPostType().getTitle(), postTypeCount.getTotalPostType());
			total += postTypeCount.getTotalPostType();
		}

		Map<String, Float> sortedMap = countTypes.entrySet().stream()
				.sorted(Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e2, e1) -> e1, LinkedHashMap::new));

		List<ShPostTypeReport> shPosTypeReports = new ArrayList<>();

		for (Entry<String, Float> types : sortedMap.entrySet()) {
			ShPostTypeReport shPostTypeReport = new ShPostTypeReport();
			shPostTypeReport.setName(types.getKey());
			shPostTypeReport.setTotal(types.getValue().intValue());
			shPostTypeReport.setColor(intToARGB(types.getKey().hashCode()));
			shPostTypeReport.setPercentage(Math.round(((types.getValue() / total) * 100.0f) * 10.0f) / 10.0f);
			shPosTypeReports.add(shPostTypeReport);

		}
		return shPosTypeReports;
	}

	public String rgbRandomColor() {
		SecureRandom random = new SecureRandom();
		return String.format("rgb(%d,%d,%d)", randomColor(random), randomColor(random), randomColor(random));
	}

	private int randomColor(SecureRandom random) {
		return random.nextInt(256) - 1;
	}

	public static String intToARGB(int i) {
		return "#".concat(Integer.toHexString(((i >> 24) & 0xFF)) + Integer.toHexString(((i >> 16) & 0xFF))
				+ Integer.toHexString(((i >> 8) & 0xFF)) + Integer.toHexString((i & 0xFF))).substring(0,7);
	}

}
