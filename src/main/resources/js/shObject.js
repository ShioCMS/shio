var shNavigationComponent = spring.getBean('shNavigationComponent', Java
		.type('com.viglet.shiohara.component.ShNavigationComponent'));
var shQueryComponent = spring.getBean('shQueryComponent', Java
		.type('com.viglet.shiohara.component.ShQueryComponent'));
var shChannelUtils = spring.getBean('shChannelUtils', Java
		.type('com.viglet.shiohara.utils.ShChannelUtils'));
var shPostUtils = spring.getBean('shPostUtils', Java
		.type('com.viglet.shiohara.utils.ShPostUtils'));
var viglet = this.viglet || {};
viglet.shiohara = viglet.shiohara || {};

/**
 * @desc the shObject class. See usage.
 * 
 * @class
 * @extends viglet.shiohara
 */
viglet.shiohara.shObject = function() {

	/**
	 * @desc Returns Channel Navigation Component
	 * @param siteName,
	 *            Site Name.
	 * @param home,
	 *            true or false to show the Home channel.
	 * @public
	 */
	this.navigation = function(siteName, home) {
		return Java.from(shNavigationComponent.navigation(siteName, home));
	},

	/**
	 * @desc Returns Query Component
	 * @param channelId,
	 *            Channel Id.
	 * @param postTypeName,
	 *            Post Type Name.
	 * @public
	 */
	this.query = function(channelId, postTypeName) {
		return Java.from(shQueryComponent.findByChannelName(channelId,
				postTypeName));
	}

	/**
	 * @desc Generate Post Link
	 * @param postId,
	 *            Post Id.
	 * @public
	 */
	this.generatePostLink = function(postId) {
		return shPostUtils.generatePostLink(postId);
	}

	/**
	 * @desc Generate Channel Link
	 * @param channelId,
	 *            Channel Id.
	 * @public
	 */
	this.generateChannelLink = function(channelId) {
		return shChannelUtils.generateChannelLink(channelId);
	}

};

viglet.shiohara.shobject = new viglet.shiohara.shObject();