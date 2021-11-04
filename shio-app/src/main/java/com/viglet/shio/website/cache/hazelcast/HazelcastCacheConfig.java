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
package com.viglet.shio.website.cache.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;

/**
 * @author Alexandre Oliveira
 */
public class HazelcastCacheConfig {

	public Config hazelCastConfig() {
		EvictionConfig evictionConfig = new EvictionConfig();
		evictionConfig.setEvictionPolicy(EvictionPolicy.LFU);
		evictionConfig.setMaxSizePolicy(EvictionConfig.DEFAULT_MAX_SIZE_POLICY);
		Config config = new Config();
		config.setInstanceName("hazelcast-cache");

		MapConfig shObject = new MapConfig();
		shObject.setTimeToLiveSeconds(86400);
		shObject.setEvictionConfig(evictionConfig);
		config.getMapConfigs().put("shObject", shObject);

		MapConfig page = new MapConfig();
		page.setTimeToLiveSeconds(86400);
		page.setEvictionConfig(evictionConfig);
		config.getMapConfigs().put("page", page);

		MapConfig pageLayout = new MapConfig();
		pageLayout.setTimeToLiveSeconds(86400);
		pageLayout.setEvictionConfig(evictionConfig);
		config.getMapConfigs().put("pageLayout", pageLayout);

		MapConfig region = new MapConfig();
		region.setTimeToLiveSeconds(86400);
		region.setEvictionConfig(evictionConfig);
		config.getMapConfigs().put("region", region);

		MapConfig javascript = new MapConfig();
		javascript.setTimeToLiveSeconds(86400);
		javascript.setEvictionConfig(evictionConfig);
		config.getMapConfigs().put("javascript", javascript);

		MapConfig component = new MapConfig();
		component.setTimeToLiveSeconds(86400);
		component.setEvictionConfig(evictionConfig);
		config.getMapConfigs().put("component", component);

		MapConfig url = new MapConfig();
		url.setTimeToLiveSeconds(86400);
		url.setEvictionConfig(evictionConfig);
		config.getMapConfigs().put("url", url);

		return config;
	}

}