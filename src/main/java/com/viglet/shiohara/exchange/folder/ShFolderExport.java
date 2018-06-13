package com.viglet.shiohara.exchange.folder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.ShFileExchange;
import com.viglet.shiohara.exchange.ShFolderExchange;
import com.viglet.shiohara.exchange.ShPostExchange;
import com.viglet.shiohara.exchange.ShPostTypeExchange;
import com.viglet.shiohara.exchange.post.ShPostExport;
import com.viglet.shiohara.exchange.post.type.ShPostTypeExport;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;

@Component
public class ShFolderExport {
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShPostExport shPostExport;
	@Autowired
	private ShPostTypeExport shPostTypeExport;

	public ShExchange shFolderExchangeIterate(List<ShFolder> shFolders) {
		ShExchange shExchange = new ShExchange();
		List<ShFolderExchange> shFolderExchanges = new ArrayList<ShFolderExchange>();
		List<ShPostExchange> shPostExchanges = new ArrayList<ShPostExchange>();
		List<ShFileExchange> files = new ArrayList<ShFileExchange>();
		Map<String, ShPostTypeExchange> shPostTypeExchanges = new HashMap<String, ShPostTypeExchange>();
		for (ShFolder shFolder : shFolders) {

			for (ShPost shPost : shPostRepository.findByShFolder(shFolder)) {
				ShPostExchange shPostExchange = new ShPostExchange();
				shPostExchange.setId(shPost.getId());
				shPostExchange.setFolder(shPost.getShFolder().getId());
				shPostExchange.setDate(shPost.getDate());
				shPostExchange.setPostType(shPost.getShPostType().getName());
				shPostExchange.setOwner(shPost.getOwner());
				shPostExchange.setFurl(shPost.getFurl());

				if (!shPostTypeExchanges.containsKey(shPost.getShPostType().getName())) {
					shPostTypeExchanges.put(shPost.getShPostType().getName(),
							shPostTypeExport.exportPostType(shPost.getShPostType()));
				}
				Map<String, Object> fields = new HashMap<String, Object>();

				shPostExport.shPostAttrExchangeIterate(shPost, shPostAttrRepository.findByShPost(shPost), fields,
						files);

				shPostExchange.setFields(fields);

				shPostExchanges.add(shPostExchange);
			}
			ShFolderExchange shFolderExchangeChild = new ShFolderExchange();
			shFolderExchangeChild.setId(shFolder.getId());
			shFolderExchangeChild.setDate(shFolder.getDate());
			shFolderExchangeChild.setName(shFolder.getName());
			shFolderExchangeChild.setOwner(shFolder.getOwner());
			shFolderExchangeChild.setFurl(shFolder.getFurl());

			if (shFolder.getParentFolder() != null) {
				shFolderExchangeChild.setParentFolder(shFolder.getParentFolder().getId());
			}
			shFolderExchanges.add(shFolderExchangeChild);
			ShExchange shExchangeChild = this.shFolderExchangeNested(shFolder);
			shFolderExchanges.addAll(shExchangeChild.getFolders());
			shPostExchanges.addAll(shExchangeChild.getPosts());

			for (ShPostTypeExchange shPostTypeExchange : shExchangeChild.getPostTypes()) {
				if (!shPostTypeExchanges.containsKey(shPostTypeExchange.getName())) {
					shPostTypeExchanges.put(shPostTypeExchange.getName(), shPostTypeExchange);
				}

			}
			files.addAll(shExchangeChild.getFiles());
		}
		shExchange.setFolders(shFolderExchanges);
		shExchange.setPosts(shPostExchanges);
		shExchange.setFiles(files);
		shExchange.setPostTypes(new ArrayList<ShPostTypeExchange>(shPostTypeExchanges.values()));
		return shExchange;
	}

	public ShExchange shFolderExchangeNested(ShFolder shFolder) {
		List<ShFolder> childFolders = shFolderRepository.findByParentFolder(shFolder);
		return this.shFolderExchangeIterate(childFolders);
	}
}
