var shioharaApp = angular.module('shioharaApp', [ 'ngCookies','ngResource', 'ngAnimate',
	'ngSanitize', 'ui.router', 'ui.bootstrap', 'pascalprecht.translate', 'vecchioOauth', 'angularMoment', 'ui.ace' ]);
shioharaApp.config([
		'$stateProvider',
		'$urlRouterProvider',
		'TokenProvider',
		'$locationProvider',
		'$translateProvider',
		function($stateProvider, $urlRouterProvider, TokenProvider,
				$locationProvider, $translateProvider) {

			TokenProvider.extendConfig({
				clientId : 'b0ec29dd6e0c6bd98b37fee1799dc0a8',
				redirectUri : 'http://localhost:8080/oauth2callback.html',
				scopes : [ "https://www.googleapis.com/auth/userinfo.email" ]
			});

			$translateProvider.useSanitizeValueStrategy('escaped');
			$translateProvider.translations('en', {				
				SEARCH: "Search",
				SEARCH_FOR: "Search for"
			});
			$translateProvider.translations('pt', {
				SEARCH: "Pesquisar",
				SEARCH_FOR: "Pesquisar por"
			});
			
			$translateProvider.fallbackLanguage('en');

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
					+ shPort;
			
			var shEmbAPIServer = shEmbServer + shAPIContext;
			console.log(shEmbServer);

			this.server = function() {

				if ($cookies.get('shServer') != null)
					return $cookies.get('shServer');
				else {
					$http({
						method : 'GET',
						url : shEmbAPIServer
					}).then(function successCallback(response) {
						$cookies.put('shServer', shEmbServer);
					}, function errorCallback(response) {
						$cookies.put('shServer', 'http://localhost:2710');

					});
					return shEmbServer;
				}
			}
			this.get = function() {

				if ($cookies.get('shAPIServer') != null)
					return $cookies.get('shAPIServer');
				else {
					$http({
						method : 'GET',
						url : shEmbAPIServer
					}).then(function successCallback(response) {
						$cookies.put('shAPIServer', shEmbAPIServer);
					}, function errorCallback(response) {
						$cookies.put('shAPIServer', 'http://localhost:2710' + shAPIContext);

					});
					return shEmbAPIServer;
				}
			}
		} ]);
shioharaApp.factory('vigLocale', [
		'$window',
		function($window) {
			return {
				getLocale : function() {
					var nav = $window.navigator;
					if (angular.isArray(nav.languages)) {
						if (nav.languages.length > 0) {
							return nav.languages[0].split('-').join('_');
						}
					}
					return ((nav.language || nav.browserLanguage
							|| nav.systemLanguage || nav.userLanguage) || '')
							.split('-').join('_');
				}
			}
		} ]);
shioharaApp.controller('ShContentCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"Token",
		"shUserResource",
		"shChannelResource",
		"shPostResource",
		"shPostTypeResource",
		'vigLocale',
		'$location',
		"$translate",
		function($scope, $http, $window, $state, $rootScope, Token,
				shUserResource, shChannelResource, shPostResource, shPostTypeResource, vigLocale, $location,
				$translate) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);

			$scope.accessToken = Token.get();
			$scope.shUser = null;
			$scope.shPosts = null;
			$scope.shLastPostType = null;
			$scope.shChannels = null;
			$rootScope.$state = $state;
			
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/site/1/channel"))
					.then(function(response) {
						$scope.shChannels = response.data;
					}));
			
			$scope.shUser = shUserResource.get({
				id : 1,
				access_token : $scope.accessToken
			}, function() {
				$scope.shLastPostType = shPostTypeResource.get({
					id : $scope.shUser.lastPostType
				});
			});
			
			$scope.shPosts = shPostResource.query();
		} ]);
shioharaApp.controller('ShPostFormCtrl', [ "$scope", "$http", "$window",
		"$stateParams", "$state", "$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postTypeAttrId = $stateParams.postTypeAttrId;
			$scope.shPostTypeItem = angular.copy($scope.shPostType);

		} ]);
shioharaApp.controller('ShPostTypeAttrCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shPostTypeAttrResource",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shPostTypeAttrResource) {
			$scope.postTypeAttrId = $stateParams.postTypeAttrId;
			$scope.shPostTypeAttr = shPostTypeAttrResource.get({
				id : $scope.postTypeAttrId
			});
			$rootScope.$state = $state;
			$scope.postTypeAttrSave = function() {
				$scope.shshPostTypeAttrPost.$update(function() {
					$state.go('content.post-type-item');
				});
			}

		} ]);
shioharaApp.controller('ShPostTypeSelectCtrl',
		[
				"$scope",
				"$http",
				"$window",
				"$state",
				"$rootScope",
				"shPostTypeResource",
				function($scope, $http, $window, $state, $rootScope,
						shPostTypeResource) {
					$rootScope.$state = $state;
					$scope.shPostTypes = shPostTypeResource.query();
				} ]);
shioharaApp.controller('ShPostTypeEditorCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"shAPIServerService",
		"shPostTypeResource",
		function($scope, $http, $window, $state, $rootScope,
				shAPIServerService, shPostTypeResource) {
			$rootScope.$state = $state;
			$scope.shPostType = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/post/type/model")).then(
					function(response) {
						$scope.shPostType = response.data;
					}));
			$scope.postTypeSave = function() {
				delete $scope.shPostType.id;
				shPostTypeResource.save($scope.shPostType, function() {
					$state.go('content.post-type-select');
				});
			}
		} ]);
shioharaApp
		.controller(
				'ShPostTypeItemCtrl',
				[
						"$scope",
						"$http",
						"$window",
						"$stateParams",
						"$state",
						"$rootScope",
						"shWidgetResource",
						"shPostTypeResource",
						"shPostTypeAttrResource",
						"shAPIServerService",
						function($scope, $http, $window, $stateParams, $state,
								$rootScope, shWidgetResource,
								shPostTypeResource, shPostTypeAttrResource,
								shAPIServerService) {
							$scope.postTypeId = $stateParams.postTypeId;
							$scope.shPostType = null;
							$scope.shWidgets = shWidgetResource.query();
							$rootScope.$state = $state;
							$scope.shPostType = shPostTypeResource
									.get(
											{
												id : $scope.postTypeId
											},
											function(response) {
												$scope.shPostNewItem = angular
														.copy($scope.shPostType);
												for (var i = 0; i < $scope.shPostNewItem.shPostTypeAttrs.length; i++) {
													$scope.shPostNewItem.shPostTypeAttrs[i]['value'] = 'Novo Valor'
															+ i;
												}
											});

							$scope.shPostTypeAttrModel = null;

							$scope
									.$evalAsync($http
											.get(
													shAPIServerService
															.get()
															.concat(
																	"/post/type/attr/model"))
											.then(
													function(response) {
														$scope.shPostTypeAttrModel = response.data;
													}));

							$scope.postTypeSave = function() {
								angular
										.forEach(
												$scope.shPostType.shPostTypeAttrs,
												function(value, key) {
													if (value.willBeDeleted == 1) {
														shPostTypeAttrResource
																.delete({
																	id : value.id
																});
														var index = $scope.shPostType.shPostTypeAttrs
																.indexOf(value);
														$scope.shPostType.shPostTypeAttrs
																.splice(index,
																		1);
													}
												});

								$scope.shPostType.$update(function() {

									$state.go('content.post-type-select');
								});

							}

							$scope.removePostType = function() {
								shPostTypeResource
								.delete({
									id : $scope.shPostType.id
								},function() {
									$state.go('content.post-type-select');
								});
							}
							
							$scope.addPostTypeAttr = function(shWidget) {
								$scope.shPostTypeAttrModel.shWidget = shWidget;
								delete $scope.shPostTypeAttrModel.id;
								$scope.shPostType.shPostTypeAttrs.push(angular
										.copy($scope.shPostTypeAttrModel));
							}
							
							$scope.removePostTypeAttr = function(shPostTypeAttr) {
								if (shPostTypeAttr.id == null
										|| shPostTypeAttr.id == 0) {
									// Removed from shPostTypeAttrs because is
									// not persisted
									var index = $scope.shPostType.shPostTypeAttrs
											.indexOf(shPostTypeAttr);
									$scope.shPostType.shPostTypeAttrs.splice(
											index, 1);
								} else {
									// Mark to be deleted
									shPostTypeAttr['willBeDeleted'] = 1;
								}
							}
						} ]);
shioharaApp.factory('shPostTypeAttrResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/post/type/attr/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
shioharaApp.factory('shPostTypeResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/post/type/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
shioharaApp.controller('ShPostNewCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shAPIServerService",
		"shPostResource",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shAPIServerService, shPostResource) {
			$scope.postTypeId = $stateParams.postTypeId;
			$scope.shPost = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/post/type/" + $scope.postTypeId + "/post/model"))
					.then(function(response) {
						$scope.shPost = response.data;
					}));
			$scope.postEditForm = "template/post/form.html";
			
			$scope.openPreviewURL = function() {
				var previewURL = shAPIServerService.server().concat(
						"/sites/SampleSite/default/pt-br/"
								+ $scope.shPost.title.replace(new RegExp(" ",
										'g'), "-"));
				 $window.open(previewURL,"_self");

			}
			
			$scope.postSave = function() {
				if ($scope.shPost.id != null && $scope.shPost.id > 0) {
					$scope.shPost.$update(function() {
						// $state.go('content');
					});
				} else {
					delete $scope.shPost.id;
					shPostResource.save($scope.shPost, function(response) {
						console.log(response);
						$scope.shPost = response;
					});
				}
			}
		} ]);
shioharaApp.factory('shPostResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/post/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
shioharaApp.controller('ShPostEditCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shPostResource",
		"shAPIServerService",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shPostResource, shAPIServerService) {
			$scope.postId = $stateParams.postId;

			$scope.shPost = shPostResource.get({
				id : $scope.postId
			});

			$scope.openPreviewURL = function() {
				var previewURL = shAPIServerService.server().concat(
						"/sites/SampleSite/default/pt-br/"
								+ $scope.shPost.title.replace(new RegExp(" ",
										'g'), "-"));
				 $window.open(previewURL,"_self");

			}
			$scope.postEditForm = "template/post/form.html";
			$scope.postDelete = function() {
				shPostResource
				.delete({
					id : $scope.shPost.id
				},function() {
					$state.go('content',{}, {reload: true});
				});
			}
			$scope.postSave = function() {
				$scope.shPost.$update(function() {
					// $state.go('content');
				});
			}
		} ]);
shioharaApp.factory('shWidgetResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/widget/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
shioharaApp.controller('ShOAuth2Ctrl', [ "$scope", "$http", "$window",
		"$state", "$rootScope", "Token",
		function($scope, $http, $window, $state, $rootScope, Token) {
			$scope.accessToken = Token.get();

			$scope.authenticate = function() {
				var extraParams = $scope.askApproval ? {
					approval_prompt : 'force'
				} : {};
				Token.getTokenByPopup(extraParams).then(function(params) {
					// Success getting token from popup.

					// Verify the token before setting it, to avoid the confused
					// deputy problem.
					Token.verifyAsync(params.access_token).then(function(data) {
						$rootScope.$apply(function() {
							$scope.accessToken = params.access_token;
							$scope.expiresIn = params.expires_in;

							Token.set(params.access_token);
						});
					}, function() {
						alert("Failed to verify token.")
					});

				}, function() {
					// Failure getting token from popup.
					alert("Failed to get token from popup.");
				});
			};
		} ]);
shioharaApp.factory('shUserResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/user/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
shioharaApp.factory('shChannelResource', [ '$resource', 'shAPIServerService',
		function($resource, shAPIServerService) {
			return $resource(shAPIServerService.get().concat('/channel/:id'), {
				id : '@id'
			}, {
				update : {
					method : 'PUT'
				}
			});
		} ]);
shioharaApp.controller('ShSiteListCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"shSiteResource",
		function($scope, $http, $window, $state, $rootScope, shSiteResource) {
			$rootScope.$state = $state;
			$scope.shSites = shSiteResource.query();
		} ]);
shioharaApp.factory('shSiteResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/site/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
