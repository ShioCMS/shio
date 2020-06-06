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
package com.viglet.shio.sites.cache.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;

/**
 * @author Alexandre Oliveira
 */
public class HazelcastCacheConfig {

	public Config hazelCastConfig() {

		Config config = new Config();
		config.setInstanceName("hazelcast-cache");

		MapConfig shobject = new MapConfig();
		shobject.setTimeToLiveSeconds(86400);
		shobject.setEvictionPolicy(EvictionPolicy.LFU);
		config.getMapConfigs().put("shobject", shobject);

		MapConfig page = new MapConfig();
		page.setTimeToLiveSeconds(86400);
		page.setEvictionPolicy(EvictionPolicy.LFU);
		config.getMapConfigs().put("page", page);

		MapConfig pageLayout = new MapConfig();
		pageLayout.setTimeToLiveSeconds(86400);
		pageLayout.setEvictionPolicy(EvictionPolicy.LFU);
		config.getMapConfigs().put("pageLayout", pageLayout);

		MapConfig region = new MapConfig();
		region.setTimeToLiveSeconds(86400);
		region.setEvictionPolicy(EvictionPolicy.LFU);
		config.getMapConfigs().put("region", region);

		MapConfig javascript = new MapConfig();
		javascript.setTimeToLiveSeconds(86400);
		javascript.setEvictionPolicy(EvictionPolicy.LFU);
		config.getMapConfigs().put("javascript", javascript);

		MapConfig component = new MapConfig();
		component.setTimeToLiveSeconds(86400);
		component.setEvictionPolicy(EvictionPolicy.LFU);
		config.getMapConfigs().put("component", component);

		MapConfig url = new MapConfig();
		url.setTimeToLiveSeconds(86400);
		url.setEvictionPolicy(EvictionPolicy.LFU);
		config.getMapConfigs().put("url", url);

		return config;
	}

}