shioharaApp.config([
		'$stateProvider',
		'$urlRouterProvider',
		'$locationProvider',
		'$translateProvider',
		'NotificationProvider',
		'$httpProvider',
		function($stateProvider, $urlRouterProvider,
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

			$translateProvider.translations('en', {
				SEARCH : "Search",
				SEARCH_FOR : "Search for",
				USERNAME: "Username",
				FIRST_NAME: "First Name",
				LAST_NAME: "Last Name",
				PASSWORD: "Password",
				SETTINGS_ACCOUNT_TITLE : "Account",
				SETTINGS_ACCOUNT_SUBTITLE : "Change your basic account and language settings.",
				SETTINGS_SAVE_CHANGES : "Save Changes"	
			});
			$translateProvider.translations('pt', {
				SEARCH : "Pesquisar",
				SEARCH_FOR : "Pesquisar por",
				USERNAME: "Nome do Usuário",
				FIRST_NAME: "Nome",
				LAST_NAME: "Sobrenome",
				PASSWORD: "Senha",		
				SETTINGS_ACCOUNT_TITLE : "Conta",
				SETTINGS_ACCOUNT_SUBTITLE : "Altere suas configurações básicas da conta e de idioma.",
				SETTINGS_SAVE_CHANGES : "Salvar Alterações"
			});

			$translateProvider.fallbackLanguage('en');

			$urlRouterProvider.otherwise('');

			$stateProvider.state('content', {	
				url : '',
				controller : 'ShContentCtrl',
				redirectTo: 'content.home'
			}).state('content.home', {
				url : '/home',
				templateUrl : 'template/content.html',
				controller : 'ShContentHomeCtrl',
				data : {
					pageTitle : 'Content | Viglet Shiohara'
				}
			}).state('content.object', {
				url : '/:objectId'
			}).state('content.children', {
				url : '/list/:objectId',
				templateUrl : 'template/object/object-children.html',
				controller :'ShObjectChildrenCtrl',				
				data : {
					pageTitle : 'Content | Viglet Shiohara'
				}
			}).state('content.ecommerce', {
				url : '/ecommerce',
				templateUrl : 'template/ecommerce/ecommerce.html',
				controller :'ShECommerceCtrl',				
				data : {
					pageTitle : 'E-Commerce | Viglet Shiohara'
				}
			}).state('content.ecommerce.payment', {
				url : '/payment',
				templateUrl : 'template/ecommerce/payment/ecommerce-payment.html',
				controller :'ShECommercePaymentCtrl',				
				data : {
					pageTitle : 'E-Commerce Payment Types | Viglet Shiohara'
				}
			}).state('content.ecommerce.setting', {
				url : '/setting',
				templateUrl : 'template/ecommerce/ecommerce-setting.html',
				controller :'ShECommerceSettingCtrl',				
				data : {
					pageTitle : 'E-Commerce Settings | Viglet Shiohara'
				}
			}).state('content.commit', {
				url : '/commit/:objectId',
				templateUrl : 'template/commit/commit.html',
				controller :'ShCommitCtrl',				
				data : {
					pageTitle : 'Commits | Viglet Shiohara'
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
				templateUrl : 'template/search/search.html',
				controller : 'ShSearchCtrl',
				data : {
					pageTitle : 'Search | Viglet Shiohara'
				}
			}).state('content.post-type-editor', {
				url : '/post/type/editor',
				templateUrl : 'template/post/type/editor.html',
				controller : 'ShPostTypeEditorCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shiohara'
				}
			}).state('content.post-type-item-by-name', {
				url : '/post/type-name/:postTypeName',
				templateUrl : 'template/post/type/item.html',
				controller : 'ShPostTypeItemCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shiohara'
				}
			}).state('content.post-type-item', {
				url : '/post/type/:postTypeId',
				templateUrl : 'template/post/type/item.html',
				controller : 'ShPostTypeItemCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shiohara'
				}
			}).state('content.post-type-item.attribute', {
				url : '/attrib/:postTypeAttrId',
				templateUrl : 'template/post/type/attribute.html',
				controller : 'ShPostTypeAttrCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shiohara'
				}
			}).state('content.post-type-item.post-item-new', {
				url : '/folder/:folderId',
				templateUrl : 'template/post/item.html',
				controller : 'ShPostNewCtrl',
				data : {
					pageTitle : 'New Post | Viglet Shiohara'
				}			
			}).state('content.post-type-item-by-name.post-item-new', {
				url : '/folder/:folderId',
				templateUrl : 'template/post/item.html',
				controller : 'ShPostNewCtrl',
				data : {
					pageTitle : 'New Post | Viglet Shiohara'
				}			
			}).state('content.post-type-item.post-item', {
				url : '/post/:postId',
				templateUrl : 'template/post/item.html',
				controller : 'ShPostEditCtrl',
				data : {
					pageTitle : 'Edit Post | Viglet Shiohara'
				}
			}).state('post-item-form', {
				url : '/post/type/:postTypeId/post/form',
				templateUrl : 'template/post/form.html',
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
				templateUrl : 'template/site/site-edit.html',
				controller : 'ShSiteEditCtrl',
				data : {
					pageTitle : 'Edit Site | Viglet Shiohara'
				}
			}).state('content.site-import', {
				url : '/site/import',
				templateUrl : 'template/site/site-import.html',
				controller : 'ShSiteImportCtrl',
				data : {
					pageTitle : 'Import Site | Viglet Shiohara'
				}
			}).state('content.object.folder-new', {
				url : '/folder',
				templateUrl : 'template/folder/folder-new.html',
				controller : 'ShFolderNewCtrl',
				data : {
					pageTitle : 'New Folder | Viglet Shiohara'
				}
			}).state('content.folder.post-type-select', {
				url : '/post/type',
				templateUrl : 'template/post/type/select-with-folder.html',
				controller : 'ShPostTypeSelectWithFolderCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shiohara'
				}
			}).state('content.post-type-select', {
				url : '/post/type',
				templateUrl : 'template/post/type/select.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shiohara'
				}
			}).state('content.post-type-system', {
				url : '/post/type/system',
				templateUrl : 'template/post/type/select-system.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shiohara'
				}
			}).state('content.site.post-type-select', {
				url : '/post/type',
				templateUrl : 'template/post/type/select.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shiohara'
				}
			}).state('content.site-new', {
				url : '/site/new',
				templateUrl : 'template/site/site-new.html',
				controller : 'ShSiteNewCtrl',
				data : {
					pageTitle : 'New Site | Viglet Shiohara'
				}
			}).state('organization', {
				url: '/org',
				templateUrl: 'template/organization/organization.html',
				data : { pageTitle: 'Organization | Viglet Shiohara' }
			}).state('organization.user', {
				url: '/user',
				templateUrl: 'template/organization/user/user.html',
				controller: 'ShUserCtrl',
				data : { pageTitle: 'Users | Viglet Shiohara' }
			}).state('organization.user-new', {
				url: '/user/new',
				templateUrl: 'template/organization/user/user-item.html',
				controller: 'ShUserNewCtrl',
				data : { pageTitle: 'New User | Viglet Shiohara' }
			}).state('organization.user-edit', {
				url: '/user/:userId',
				templateUrl: 'template/organization/user/user-item.html',
				controller: 'ShUserEditCtrl',
				data : { pageTitle: 'Edit User | Viglet Shiohara' }
			}).state('organization.role', {
				url: '/role',
				templateUrl: 'template/organization/role/role.html',
				controller: 'ShRoleCtrl',
				data : { pageTitle: 'Roles | Viglet Shiohara' }
			}).state('organization.role-new', {
				url: '/role/new',
				templateUrl: 'template/organization/role/role-item.html',
				controller: 'ShRoleNewCtrl',
				data : { pageTitle: 'New Role | Viglet Shiohara' }
			}).state('organization.role-edit', {
				url: '/role/:roleId',
				templateUrl: 'template/organization/role/role-item.html',
				controller: 'ShRoleEditCtrl',
				data : { pageTitle: 'Edit Role | Viglet Shiohara' }
			}).state('organization.group', {
				url: '/group',
				templateUrl: 'template/organization/group/group.html',
				controller: 'ShGroupCtrl',
				data : { pageTitle: 'Groups | Viglet Shiohara' }
			}).state('organization.group-new', {
				url: '/group/new',
				templateUrl: 'template/organization/group/group-item.html',
				controller: 'ShGroupNewCtrl',
				data : { pageTitle: 'New Group | Viglet Shiohara' }
			}).state('organization.group-edit', {
				url: '/group/:groupId',
				templateUrl: 'template/organization/group/group-item.html',
				controller: 'ShGroupEditCtrl',
				data : { pageTitle: 'Edit Group | Viglet Shiohara' }
			})

		} ]);



shPreviewApp.config([
	'$stateProvider',
	'$urlRouterProvider',
	'$locationProvider',
	'$translateProvider',
	'NotificationProvider',
	'$httpProvider',
	function($stateProvider, $urlRouterProvider,
			$locationProvider, $translateProvider, NotificationProvider,$httpProvider) {	

		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
		 
		$translateProvider.useSanitizeValueStrategy('escaped');
	
		$urlRouterProvider.otherwise('');

		$stateProvider.state('preview', {
			url : '/:objectId',
			templateUrl : '../template/preview/preview.html',
			controller :'ShPreviewCtrl',				
			data : {
				pageTitle : 'Preview | Viglet Shiohara'
			}
		})
	} ]);

