package com.viglet.shiohara.bean.xp;

import com.viglet.shiohara.persistence.model.post.ShPost;

public class ShPostXP {

	private ShPost shPost;
	
	private boolean allowPublish;

	public ShPost getShPost() {
		return shPost;
	}

	public void setShPost(ShPost shPost) {
		this.shPost = shPost;
	}

	public boolean isAllowPublish() {
		return allowPublish;
	}

	public void setAllowPublish(boolean allowPublish) {
		this.allowPublish = allowPublish;
	} 
	
	
}
