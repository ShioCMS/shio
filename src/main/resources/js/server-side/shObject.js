var shNavigationComponent = spring.getBean('shNavigationComponent', Java
		.type('com.viglet.shiohara.component.ShNavigationComponent'));
var shQueryComponent = spring.getBean('shQueryComponent', Java
		.type('com.viglet.shiohara.component.ShQueryComponent'));
var shFolderUtils = spring.getBean('shFolderUtils', Java
		.type('com.viglet.shiohara.utils.ShFolderUtils'));
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
	 * @desc Returns Folder Navigation Component
	 * @param siteName,
	 *            Site Name.
	 * @param home,
	 *            true or false to show the Home folder.
	 * @public
	 */
	this.navigation = function(siteName, home) {
		return Java.from(shNavigationComponent.navigation(siteName, home));
	},

	/**
	 * @desc Returns Query Component
	 * @param folderId,
	 *            Folder Id.
	 * @param postTypeName,
	 *            Post Type Name.
	 * @public
	 */
	this.query = function(folderId, postTypeName) {
		return Java.from(shQueryComponent.findByFolderName(folderId,
				postTypeName));
	}

	/**
	 * @desc Generate Post Link
	 * @param postId,
	 *            Post Id.
	 * @public
	 */
	this.generatePostLink = function(postId) { 
		return shPostUtils.generatePostLinkById(postId);
	}

	/**
	 * @desc Generate Folder Link
	 * @param folderId,
	 *            Folder Id.
	 * @public
	 */
	this.generateFolderLink = function(folderId) {
		return shFolderUtils.generateFolderLinkById(folderId);
	}

};

viglet.shiohara.shobject = new viglet.shiohara.shObject();
