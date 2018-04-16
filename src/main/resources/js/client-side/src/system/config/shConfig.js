shioharaApp.config([
		'$stateProvider',
		'$urlRouterProvider',
		'TokenProvider',
		'$locationProvider',
		'$translateProvider',
		'NotificationProvider',
		'$httpProvider',
		function($stateProvider, $urlRouterProvider, TokenProvider,
				$locationProvider, $translateProvider, NotificationProvider,$httpProvider) {	
			
			$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
			 
			$translateProvider.useSanitizeValueStrategy('escaped');
			
			NotificationProvider.setOptions({
				delay : 5000,
				startTop : 20,
				startRight : 10,
				verticalSpacing : 20,
				horizontalSpacing : 20,
				positionX : 'right',
				positionY : 'bottom'
			});
			TokenProvider.extendConfig({
				clientId : 'b0ec29dd6e0c6bd98b37fee1799dc0a8',
				redirectUri : 'http://localhost:8080/oauth2callback.html',
				scopes : [ "https://www.googleapis.com/auth/userinfo.email" ]
			});

			$translateProvider.translations('en', {
				SEARCH : "Search",
				SEARCH_FOR : "Search for"
			});
			$translateProvider.translations('pt', {
				SEARCH : "Pesquisar",
				SEARCH_FOR : "Pesquisar por"
			});

			$translateProvider.fallbackLanguage('en');

			$urlRouterProvider.otherwise('');

			$stateProvider.state('oauth2', {
				url : '/oauth2',
				templateUrl : '/template/oauth2.html',
				controller : 'ShOAuth2Ctrl',
				data : {
					pageTitle : 'OAuth2 | Viglet Shiohara'
				}
			}).state('content', {	
				url : '',
				controller : 'ShContentCtrl',
				redirectTo: 'content.home'
			}).state('content.home', {
				url : '/home',
				templateUrl : '/template/content.html',
				controller : 'ShContentHomeCtrl',
				data : {
					pageTitle : 'Content | Viglet Shiohara'
				}
			}).state('content.object', {
				url : '/:objectId'
			})
			.state('content.children', {
				url : '/list/:objectId',
				templateUrl : '/template/object/object-children.html',
				controller :'ShObjectChildrenCtrl',				
				data : {
					pageTitle : 'Content | Viglet Shiohara'
				}
			}).state('content.search', {
				url : '/search',
				templateUrl : '/template/search/search.html',
				controller : 'ShSearchCtrl',
				data : {
					pageTitle : 'Search | Viglet Shiohara'
				}
			}).state('content.search-query', {
				url : '/search/:query',
				templateUrl : '/template/search/search.html',
				controller : 'ShSearchCtrl',
				data : {
					pageTitle : 'Search | Viglet Shiohara'
				}
			}).state('content.post-type-editor', {
				url : '/post/type/editor',
				templateUrl : '/template/post/type/editor.html',
				controller : 'ShPostTypeEditorCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shiohara'
				}
			}).state('content.post-type-item', {
				url : '/post/type/:postTypeId',
				templateUrl : '/template/post/type/item.html',
				controller : 'ShPostTypeItemCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shiohara'
				}
			}).state('content.post-type-item.attribute', {
				url : '/attrib/:postTypeAttrId',
				templateUrl : '/template/post/type/attribute.html',
				controller : 'ShPostTypeAttrCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shiohara'
				}
			}).state('content.post-type-item.post-item-new', {
				url : '/folder/:folderId',
				templateUrl : '/template/post/item.html',
				controller : 'ShPostNewCtrl',
				data : {
					pageTitle : 'New Post | Viglet Shiohara'
				}
			}).state('content.post-type-item.post-item', {
				url : '/post/:postId',
				templateUrl : '/template/post/item.html',
				controller : 'ShPostEditCtrl',
				data : {
					pageTitle : 'Edit Post | Viglet Shiohara'
				}
			}).state('post-item-form', {
				url : '/post/type/:postTypeId/post/form',
				templateUrl : '/template/post/form.html',
				controller : 'ShPostFormCtrl',
				data : {}
			}).state('content.folder', {
				url : '/folder/:folderId'
			}).state('content.folder.edit', {
				url : '/edit',
				templateUrl : '/template/folder/folder-edit.html',
				controller : 'ShFolderEditCtrl',
				data : {
					pageTitle : 'Edit Folder | Viglet Shiohara'
				}
			}).state('content.site', {
				url : '/site/:siteId'
			}).state('content.site.edit', {
				url : '/edit',
				templateUrl : '/template/site/site-edit.html',
				controller : 'ShSiteEditCtrl',
				data : {
					pageTitle : 'Edit Site | Viglet Shiohara'
				}
			}).state('content.site-import', {
				url : '/site/import',
				templateUrl : '/template/site/site-import.html',
				controller : 'ShSiteImportCtrl',
				data : {
					pageTitle : 'Import Site | Viglet Shiohara'
				}
			}).state('content.object.folder-new', {
				url : '/folder',
				templateUrl : '/template/folder/folder-new.html',
				controller : 'ShFolderNewCtrl',
				data : {
					pageTitle : 'New Folder | Viglet Shiohara'
				}
			}).state('content.folder.post-type-select', {
				url : '/post/type',
				templateUrl : '/template/post/type/select-with-folder.html',
				controller : 'ShPostTypeSelectWithFolderCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shiohara'
				}
			}).state('content.post-type-select', {
				url : '/post/type',
				templateUrl : '/template/post/type/select.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shiohara'
				}
			}).state('content.site.post-type-select', {
				url : '/post/type',
				templateUrl : '/template/post/type/select.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shiohara'
				}
			}).state('content.site-new', {
				url : '/site/new',
				templateUrl : '/template/site/site-new.html',
				controller : 'ShSiteNewCtrl',
				data : {
					pageTitle : 'New Site | Viglet Shiohara'
				}
			})

		} ]);
