shioApp.config([
		'$stateProvider',
		'$urlRouterProvider',
		'$locationProvider',
		'$translateProvider',
		'NotificationProvider',
		'$httpProvider',
		function($stateProvider, $urlRouterProvider,
				$locationProvider, $translateProvider, NotificationProvider, $httpProvider) {	

			$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
			$httpProvider.interceptors.push('shAuthInterceptor');
			
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
				templateUrl : 'template/content-base.html',
				redirectTo: 'content.home'
			}).state('content.home', {
				url : '/home',
				controller : 'ShContentHomeCtrl',
				templateUrl : 'template/content-home.html',
				data : {
					pageTitle : 'Content | Viglet Shio CMS'
				}
			}).state('content.object', {
				url : '/:objectId'
			}).state('content.children', {
				url : '/list/:objectId',
				templateUrl : 'template/object/object-children.html',
				controller :'ShObjectChildrenCtrl',				
				data : {
					pageTitle : 'Content | Viglet Shio CMS'
				}
			}).state('content.ecommerce.setting', {
				url : '/setting',
				templateUrl : 'template/ecommerce/ecommerce-setting.html',
				controller :'ShECommerceSettingCtrl',				
				data : {
					pageTitle : 'E-Commerce Settings | Viglet Shio CMS'
				}
			}).state('content.commit', {
				url : '/commit/:objectId',
				templateUrl : 'template/commit/commit.html',
				controller :'ShCommitCtrl',				
				data : {
					pageTitle : 'Commits | Viglet Shio CMS'
				}
			}).state('content.search', {
				url : '/search',
				templateUrl : '/template/search/search.html',
				controller : 'ShSearchCtrl',
				data : {
					pageTitle : 'Search | Viglet Shio CMS'
				}
			}).state('content.search-type', {
				url : '/search/type/:type',
				templateUrl : 'template/search/search.html',
				controller : 'ShSearchTypeCtrl',
				data : {
					pageTitle : 'Search | Viglet Shio CMS'
				}
			}).state('content.search-query', {
				url : '/search/:query',
				templateUrl : 'template/search/search.html',
				controller : 'ShSearchCtrl',
				data : {
					pageTitle : 'Search | Viglet Shio CMS'
				}
			}).state('content.post-type-editor', {
				url : '/post/type/editor',
				templateUrl : 'template/post/type/editor.html',
				controller : 'ShPostTypeEditorCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shio CMS'
				}
			}).state('content.post-type-item-by-name', {
				url : '/post/type-name/:postTypeName',
				templateUrl : 'template/post/type/item.html',
				controller : 'ShPostTypeItemCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shio CMS'
				}
			}).state('content.post-type-item', {
				url : '/post/type/:postTypeId',
				templateUrl : 'template/post/type/item.html',
				controller : 'ShPostTypeItemCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shio CMS'
				}
			}).state('content.post-type-item.attribute', {
				url : '/attrib/:postTypeAttrId',
				templateUrl : 'template/post/type/attribute.html',
				controller : 'ShPostTypeAttrCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shio CMS'
				}
			}).state('content.post-type-item.post-item-new', {
				url : '/folder/:folderId',
				templateUrl : 'template/post/item.html',
				controller : 'ShPostNewCtrl',
				data : {
					pageTitle : 'New Post | Viglet Shio CMS'
				}			
			}).state('content.post-type-item-by-name.post-item-new', {
				url : '/folder/:folderId',
				templateUrl : 'template/post/item.html',
				controller : 'ShPostNewCtrl',
				data : {
					pageTitle : 'New Post | Viglet Shio CMS'
				}			
			}).state('content.post-type-item.post-item', {
				url : '/post/:postId',
				templateUrl : 'template/post/item.html',
				controller : 'ShPostEditCtrl',
				data : {
					pageTitle : 'Edit Post | Viglet Shio CMS'
				}
			}).state('post-item-form', {
				url : '/post/type/:postTypeId/post/form',
				templateUrl : 'template/post/form.html',
				controller : 'ShPostFormCtrl',
				data : {}
			}).state('post-item-modal', {
				url : '/modal/post/type/:postTypeId/post/:postId',
				templateUrl : 'template/post/post-modal.html',
				controller : 'ShPostEditCtrl',
				data : {}
			}).state('content.folder', {
				url : '/folder/:folderId'
			}).state('content.folder.edit', {
				url : '/edit',
				templateUrl : '/template/folder/folder-edit.html',
				controller : 'ShFolderEditCtrl',
				data : {
					pageTitle : 'Edit Folder | Viglet Shio CMS'
				}
			}).state('content.site', {
				url : '/site/:siteId'
			}).state('content.site.edit', {
				url : '/edit',
				templateUrl : 'template/site/site-edit.html',
				controller : 'ShSiteEditCtrl',
				data : {
					pageTitle : 'Edit Site | Viglet Shio CMS'
				}
			}).state('content.site-import', {
				url : '/site/import',
				templateUrl : 'template/site/site-import.html',
				controller : 'ShSiteImportCtrl',
				data : {
					pageTitle : 'Import Site | Viglet Shio CMS'
				}
			}).state('content.object.folder-new', {
				url : '/folder',
				templateUrl : 'template/folder/folder-new.html',
				controller : 'ShFolderNewCtrl',
				data : {
					pageTitle : 'New Folder | Viglet Shio CMS'
				}
			}).state('content.folder.post-type-select', {
				url : '/post/type',
				templateUrl : 'template/post/type/select-with-folder.html',
				controller : 'ShPostTypeSelectWithFolderCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shio CMS'
				}
			}).state('content.post-type-select', {
				url : '/post/type',
				templateUrl : 'template/post/type/select.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shio CMS'
				}
			}).state('content.post-type-system', {
				url : '/post/type/system',
				templateUrl : 'template/post/type/select-system.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shio CMS'
				}
			}).state('content.site.post-type-select', {
				url : '/post/type',
				templateUrl : 'template/post/type/select.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shio CMS'
				}
			}).state('content.site-new', {
				url : '/site/new',
				templateUrl : 'template/site/site-new.html',
				controller : 'ShSiteNewCtrl',
				data : {
					pageTitle : 'New Site | Viglet Shio CMS'
				}
			}).state('config', {
				url: '/config',
				templateUrl: 'template/config/config-home.html',
				controller : 'ShConfigCtrl',
				redirectTo: 'config.auth-providers',
				data : { pageTitle: 'Configuration | Viglet Shio CMS' }
			}).state('config.email', {
				url: '/email',
				templateUrl: 'template/config/email/config-email.html',
				controller: 'ShConfigEmailCtrl',
				data : { pageTitle: 'Email Configuration | Viglet Shio CMS' }
			}).state('config.search', {
				url: '/search',
				templateUrl: 'template/config/search/config-search.html',
				controller: 'ShConfigSearchCtrl',
				data : { pageTitle: 'Search Configuration | Viglet Shio CMS' }
			}).state('config.exchange-providers', {
				url: '/provider/exchange',
				templateUrl: 'template/config/provider/exchange/config-exchange-providers.html',
				controller: 'ShConfigExchangeProvidersCtrl',
				data : { pageTitle: 'Exchange Providers| Viglet Shio CMS' }
			}).state('config.exchange-provider-new', {
				url: '/provider/exchange/new',
				templateUrl: 'template/config/provider/exchange/config-exchange-provider-item.html',
				controller: 'ShConfigExchangeProviderCtrl',
				data : { pageTitle: 'New Exchange Provider| Viglet Shio CMS' }
			}).state('config.exchange-provider', {
				url: '/provider/exchange/:exchangeProviderId',
				templateUrl: 'template/config/provider/exchange/config-exchange-provider-item.html',
				controller: 'ShConfigExchangeProviderCtrl',
				data : { pageTitle: 'Edit Exchange Provider| Viglet Shio CMS' }
			}).state('config.auth-providers', {
				url: '/provider/auth',
				templateUrl: 'template/config/provider/auth/config-auth-providers.html',
				controller: 'ShConfigAuthProvidersCtrl',
				data : { pageTitle: 'Auth Providers | Viglet Shio CMS' }
			}).state('config.auth-provider-new', {
				url: '/provider/auth/new',
				templateUrl: 'template/config/provider/auth/config-auth-provider-item.html',
				controller: 'ShConfigAuthProviderCtrl',
				data : { pageTitle: 'New Auth Provider| Viglet Shio CMS' }
			}).state('config.auth-provider', {
				url: '/provider/auth/:authProviderId',
				templateUrl: 'template/config/provider/auth/config-auth-provider-item.html',
				controller: 'ShConfigAuthProviderCtrl',
				data : { pageTitle: 'Edit Auth Provider| Viglet Shio CMS' }
			}).state('admin', {
				url: '/admin',
				templateUrl: 'template/admin/admin-home.html',
				controller : 'ShAdminCtrl',
				redirectTo: 'admin.user',
				data : { pageTitle: 'Administration | Viglet Shio CMS' }
			}).state('admin.user', {
				url: '/user',
				templateUrl: 'template/admin/user/user.html',
				controller: 'ShUserCtrl',
				data : { pageTitle: 'Users | Viglet Shio CMS' }
			}).state('admin.user-new', {
				url: '/user/new',
				templateUrl: 'template/admin/user/user-item.html',
				controller: 'ShUserNewCtrl',
				data : { pageTitle: 'New User | Viglet Shio CMS' }
			}).state('admin.user-edit', {
				url: '/user/:userId',
				templateUrl: 'template/admin/user/user-item.html',
				controller: 'ShUserEditCtrl',
				data : { pageTitle: 'Edit User | Viglet Shio CMS' }
			}).state('admin.role', {
				url: '/role',
				templateUrl: 'template/admin/role/role.html',
				controller: 'ShRoleCtrl',
				data : { pageTitle: 'Roles | Viglet Shio CMS' }
			}).state('admin.role-new', {
				url: '/role/new',
				templateUrl: 'template/admin/role/role-item.html',
				controller: 'ShRoleNewCtrl',
				data : { pageTitle: 'New Role | Viglet Shio CMS' }
			}).state('admin.role-edit', {
				url: '/role/:roleId',
				templateUrl: 'template/admin/role/role-item.html',
				controller: 'ShRoleEditCtrl',
				data : { pageTitle: 'Edit Role | Viglet Shio CMS' }
			}).state('admin.group', {
				url: '/group',
				templateUrl: 'template/admin/group/group.html',
				controller: 'ShGroupCtrl',
				data : { pageTitle: 'Groups | Viglet Shio CMS' }
			}).state('admin.group-new', {
				url: '/group/new',
				templateUrl: 'template/admin/group/group-item.html',
				controller: 'ShGroupNewCtrl',
				data : { pageTitle: 'New Group | Viglet Shio CMS' }
			}).state('admin.group-edit', {
				url: '/group/:groupId',
				templateUrl: 'template/admin/group/group-item.html',
				controller: 'ShGroupEditCtrl',
				data : { pageTitle: 'Edit Group | Viglet Shio CMS' }
			}).state('playground', {
				url: '/playground',
				templateUrl: 'template/playground/playground.html',
				controller : 'ShPlaygroundCtrl',
				data : { pageTitle: 'API Playground | Viglet Shio CMS' }
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
				pageTitle : 'Preview | Viglet Shio CMS'
			}
		})
	} ]);

