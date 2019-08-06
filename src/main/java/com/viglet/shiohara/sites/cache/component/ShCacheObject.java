package com.viglet.shiohara.sites.cache.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.sites.ShSitesContextURL;
import com.viglet.shiohara.utils.ShFolderUtils;

@Component
public class ShCacheObject {
	private static final Log logger = LogFactory.getLog(ShCacheObject.class);
	@Autowired
	ShCachePage shCachePage;
	@Autowired
	ShCacheObject shCacheObject;
	@Autowired
	ShObjectRepository shObjectRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShFolderUtils shFolderUtils;

	@Cacheable(value = "shObject", key = "#id", sync = true)
	public List<String> cache(String id) {
		if (logger.isDebugEnabled())
			logger.debug("Creating the shObject Cache id " + id);
		return new ArrayList<String>();
	}

	@CachePut(value = "shObject", key = "#id")
	public List<String> updateCache(String id, ShSitesContextURL shSitesContextURL) {
		List<String> urls = shCacheObject.cache(id);
		if (!urls.contains(shSitesContextURL.getInfo().getContextURLOriginal())) {
			if (logger.isDebugEnabled())
				logger.debug("Adding id: " + id + " and URL: " + shSitesContextURL.getInfo().getContextURLOriginal());
			urls.add(shSitesContextURL.getInfo().getContextURLOriginal());
		}
		return urls;
	}

	public void deleteCache(String id) {
		ShObject shObject = shObjectRepository.findById(id).orElse(null);
		String objectId = id;
		if (shObject instanceof ShFolder) {
			ShPost shFolderIndex = shPostRepository.findByShFolderAndFurl((ShFolder) shObject, "index");
			if (shFolderIndex != null) {
				objectId = shFolderIndex.getId();
			}
		} else if (shObject instanceof ShPost) {
			ShFolder shFolder = shFolderUtils.getParentFolder(shObject);
			this.deleteCache(shFolder.getId());
		}

		this.deleteDependency(objectId);
		shCacheObject.deleteCacheSelf(objectId);

	}

	public void deleteDependency(String id) {
		if (logger.isDebugEnabled())
			logger.debug("Executing deleteDependency for id: " + id);
		List<String> urls = shCacheObject.cache(id);
		for (String url : urls) {
			if (logger.isDebugEnabled())
				logger.debug("Deleting the page with id: " + id + " and URL: " + url);
			shCachePage.deleteCache(id, url);
		}

	}

	@CacheEvict(value = "shObject", key = "#id")
	public void deleteCacheSelf(String id) {
	}
}
