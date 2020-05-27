var viglet = this.viglet || {};
viglet.shio = viglet.shio || {};

/**
 * @desc the shObject class. See usage.
 * 
 * @class
 * @extends viglet.shio
 */
viglet.shio.shObject = function() {
	/**
	 * @desc Returns Form from PostType
	 * @public
	 */
	this.formComponent = function(shPostTypeName, shObjectId) {
		return shFormComponent.byPostType(shPostTypeName, shObjectId, request);
	},	
	/**
	 * @desc Returns Search Result
	 * @public
	 */
	this.searchComponent = function() {
		return Java.from(shSearchComponent.search(request.getParameter('q')));
	},

	/**
	 * @desc Returns Folder Navigation Component
	 * @param siteName
	 *            Site Name.
	 * @param home
	 *            true or false to show the Home folder.
	 * @public
	 */
	this.navigation = function(siteName, home) {
		return Java.from(shNavigationComponent.navigation(siteName, home));
	},

	/**
	 * @desc Returns Folder Navigation Component from Parent Folder
	 * @param folderId
	 *            Folder Id.
	 * @param home
	 *            true or false to show the Home folder.
	 * @public
	 */
	this.navigationFolder = function(folderId, home) {
		return Java
				.from(shNavigationComponent.navigationFolder(folderId, home));
	},
	/**
	 * @desc Returns Query Component
	 * @param folderId
	 *            Folder Id.
	 * @param postTypeName
	 *            Post Type Name.
	 * @public
	 */
	this.query = function(folderId, postTypeName) {
		return Java.from(shQueryComponent.findByFolderName(folderId,
				postTypeName));
	}


	/**
	 * @desc Returns Query Component By Post Type
	 * @param postTypeName
	 *            Post Type Name.         
	 * @public
	 */
	this.queryByPostType = function(postTypeName) {
		return Java.from(shQueryComponent.findByPostTypeName(postTypeName));
	}
	
	/**
	 * @desc Returns Query Component By Posts that have some values
	 * @param postTypeName
	 *            Post Type Name.
	 * @param postAttrName
	 *            Post Type Attribute Name.
	 * @param arrayValue
	 *            Array Value.            
	 * @public
	 */
	this.queryByPostTypeIn = function(postTypeName, postAttrName, arrayValue) {
		return Java.from(shQueryComponent.findByPostTypeNameIn(postTypeName, postAttrName, arrayValue));
	}
	
	/**
	 * @desc Returns getRelation Component
	 * @param shPostAttrId,
	 *            Post Attribute Id.
	 * @public
	 */
	this.getRelation = function(shPostAttrId) {
		return Java.from(shGetRelationComponent.findByPostAttrId(shPostAttrId));
	}
	
	/**
	 * @desc Generate Post Link
	 * @param postId
	 *            Post Id.
	 * @public
	 */
	this.generatePostLink = function(postId) {
		return shSitesPostUtils.generatePostLinkById(postId);
	}

	/**
	 * @desc Generate Folder Link
	 * @param folderId
	 *            Folder Id.
	 * @public
	 */
	this.generateFolderLink = function(folderId) {
		return shSitesFolderUtils.generateFolderLinkById(folderId);
	}

	/**
	 * @desc Get Post Map
	 * @param postId
	 *            Post Id.
	 * @public
	 */
	this.getPost = function(postId) {
		return shSitesPostUtils.toMap(postId);
	}
	
	/**
	 * @desc Get Folder Map
	 * @param folderId,
	 *            Folder Id.
	 * @public
	 */
	this.getFolderMap = function(folderId) {
		return shSitesFolderUtils.toMap(folderId);
	}
	
	/**
	 * @desc Get Parent Folder Map
	 * @param folderId
	 *            Folder Id.
	 * @public
	 */
	this.getParentFolder = function(folderId) {
		return shSitesFolderUtils.toMap(shSitesFolderUtils.getParentFolder(folderId));
	}
	/**
	 * @desc Generate Object Link
	 * @param objectId
	 *            Object Id.
	 * @public
	 */
	this.generateObjectLink = function(objectId) {
		return shSitesObjectUtils.generateObjectLinkById(objectId);
	}
	
	/**
	 * @desc Generate Image Link
	 * @param objectId
	 *            Object Id.
	 * @param scale
	 *            scale.
	 * @public
	 */
	this.generateImageLink = function(objectId, scale) {
		return shSitesObjectUtils.generateImageLinkById(objectId, scale);
	}

};

viglet.shio.shobject = new viglet.shio.shObject();
var shObject = viglet.shio.shobject;
var forEach = Array.prototype.forEach;

