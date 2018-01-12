shioharaApp.config([ '$stateProvider', '$urlRouterProvider', 'TokenProvider',
		function($stateProvider, $urlRouterProvider, TokenProvider) {

			TokenProvider.extendConfig({
				clientId : 'b0ec29dd6e0c6bd98b37fee1799dc0a8',
				redirectUri : 'http://localhost:8080/oauth2callback.html',
				scopes : [ "https://www.googleapis.com/auth/userinfo.email" ]
			});

			$urlRouterProvider.otherwise('/content');
			$stateProvider.state('oauth2', {
				url : '/oauth2',
				templateUrl : 'template/oauth2.html',
				controller : 'ShOAuth2Ctrl',
				data : {
					pageTitle : 'OAuth2 | Viglet Shiohara'
				}
			}).state('content', {
				url : '/content',
				templateUrl : 'template/content.html',
				controller : 'ShContentCtrl',
				data : {
					pageTitle : 'Content | Viglet Shiohara'
				}
			}).state('content.post-type-select', {
				url : '/post/type/select',
				templateUrl : 'template/post/type/select.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Select | Viglet Shiohara'
				}
			}).state('content.post-type-editor', {
				url : '/post/type/editor',
				templateUrl : 'template/post/type/editor.html',
				controller : 'ShPostTypeEditorCtrl',
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
				url : '/post/new',
				templateUrl : 'template/post/item.html',
				controller : 'ShPostNewCtrl',
				data : {
					pageTitle : 'Post New | Viglet Shiohara'
				}
			}).state('content.post-type-item.post-item', {
				url : '/post/:postId',
				templateUrl : 'template/post/item.html',
				controller : 'ShPostEditCtrl',
				data : {
					pageTitle : 'Post New | Viglet Shiohara'
				}
			}).state('post-item-form', {
				url : '/post/type/:postTypeId/post/form',
				templateUrl : 'template/post/form.html',
				controller : 'ShPostFormCtrl'
			})

		} ]);
shioharaApp.service('shAPIServerService', [
		'$http',
		'$location',
		'$cookies',
		function($http, $location, $cookies) {
			var shProtocol = $location.protocol();
			var shHostname = $location.host();
			var shPort = $location.port();
			var shAPIContext = "/api";
			var shEmbServer = shProtocol + "://" + shHostname + ":"
					+ shPort + shAPIContext;
			console.log(shEmbServer);

			this.get = function() {

				if ($cookies.get('shAPIServer') != null)
					return $cookies.get('shAPIServer');
				else {
					$http({
						method : 'GET',
						url : shEmbServer
					}).then(function successCallback(response) {
						$cookies.put('shAPIServer', shEmbServer);
					}, function errorCallback(response) {
						$cookies.put('shAPIServer', 'http://localhost:2710' + shAPIContext);

					});
					return shEmbServer;
				}
			}
		} ]);
