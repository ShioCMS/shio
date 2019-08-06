package com.viglet.shiohara.cache.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class HazelcastCacheConfig {

	//@Bean
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