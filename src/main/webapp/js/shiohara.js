var shioharaApp = angular.module('shioharaApp', [ 'ui.router', 'vecchioOauth' ]);

shioharaApp.config(function($stateProvider, $urlRouterProvider, TokenProvider) {

	TokenProvider.extendConfig({
		clientId : 'b0ec29dd6e0c6bd98b37fee1799dc0a8',
		redirectUri : 'http://localhost:8080/oauth2callback.html',
		scopes : [ "https://www.googleapis.com/auth/userinfo.email" ]
	});

	$urlRouterProvider.otherwise('/content');
	$stateProvider.state('oauth2', {
		url : '/oauth2',
		templateUrl : 'template/oauth2.html',
		controller : 'VecOAuth2Ctrl',
		data : {
			pageTitle : 'OAuth2 | Viglet Shiohara'
		}
	}).state('content', {
		url : '/content',
		templateUrl : 'template/content.html',
		controller : 'VecContentCtrl',
		data : {
			pageTitle : 'Content | Viglet Shiohara'
		}
	}).state('content.post-type-select', {
		url : '/post/type/select',
		templateUrl : 'template/post/type/select.html',
		controller : 'VecPostTypeSelectCtrl',
		data : {
			pageTitle : 'Post Type Select | Viglet Shiohara'
		}
	}).state('content.post-type-editor', {
		url : '/post/type/select',
		templateUrl : 'template/post/type/editor.html',
		controller : 'VecPostTypeSelectCtrl',
		data : {
			pageTitle : 'Post Type Editor | Viglet Shiohara'
		}
	}).state('content.post-type-item', {
		url : '/post/type/:postTypeId',
		templateUrl : 'template/post/type/item.html',
		controller : 'VecPostTypeItemCtrl',
		data : {
			pageTitle : 'Post Type Editor | Viglet Shiohara'
		}
	}).state('content.post-type-item.attribute', {
		url : '/attrib/:postTypeAttrId',
		templateUrl : 'template/post/type/attribute.html',
		controller : 'VecPostTypeAttrCtrl',
		data : {
			pageTitle : 'Post Type Editor | Viglet Shiohara'
		}
	}).state('content.post-type-item.post-item-new', {
		url : '/post/new',
		templateUrl : 'template/post/item.html',
		controller : 'VecPostNewCtrl',
		data : {
			pageTitle : 'Post New | Viglet Shiohara'
		}
	}).state('content.post-type-item.post-item', {
		url : '/post/:postId',
		templateUrl : 'template/post/item.html',
		controller : 'VecPostEditCtrl',
		data : {
			pageTitle : 'Post New | Viglet Shiohara'
		}
	}).state('post-item-form', {
		url : '/post/type/:postTypeId/post/form',
		templateUrl : 'template/post/form.html',
		controller : 'VecPostFormCtrl'
	})

});

shioharaApp.controller('VecOAuth2Ctrl', [ "$scope", "$http", "$window",
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

shioharaApp.controller('VecContentCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"Token",
		function($scope, $http, $window, $state, $rootScope, Token) {
			$scope.accessToken = Token.get();
			$scope.shUser = null;
			$scope.shPosts = null;
			$rootScope.$state = $state;
			$scope.$evalAsync($http.get(jp_domain + "/api/user/2?access_token=" + $scope.accessToken).then(
					function(response) {
						$scope.shUser = response.data;
					}));
			$scope.$evalAsync($http.get(jp_domain + "/api/post").then(
					function(response) {
						$scope.shPosts = response.data;
					}));
		} ]);
shioharaApp.controller('VecSiteListCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $state, $rootScope) {
			$scope.shSites = null;
			$rootScope.$state = $state;
			$scope.$evalAsync($http.get(jp_domain + "/api/site").then(
					function(response) {
						$scope.shSites = response.data;
					}));
		} ]);

shioharaApp.controller('VecPostTypeSelectCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $state, $rootScope) {
			$scope.shPostTypes = null;
			$rootScope.$state = $state;
			$scope.$evalAsync($http.get(jp_domain + "/api/post/type").then(
					function(response) {
						$scope.shPostTypes = response.data;
					}));
		} ]);

shioharaApp
		.controller(
				'VecPostTypeItemCtrl',
				[
						"$scope",
						"$http",
						"$window",
						"$stateParams",
						"$state",
						"$rootScope",
						function($scope, $http, $window, $stateParams, $state,
								$rootScope) {
							$scope.postTypeId = $stateParams.postTypeId;
							$scope.shPostType = null;
							$scope.shWidgets = null;
							$rootScope.$state = $state;
							$scope
									.$evalAsync($http
											.get(
													jp_domain
															+ "/api/post/type/"
															+ $scope.postTypeId)
											.then(
													function(response) {
														$scope.shPostType = response.data;
														$scope.shPostNewItem = angular
																.copy($scope.shPostType);
														for (var i = 0; i < $scope.shPostNewItem.shPostTypeAttrs.length; i++) {
															$scope.shPostNewItem.shPostTypeAttrs[i]['value'] = 'Novo Valor'
																	+ i;
														}

													}));
							$scope.$evalAsync($http.get(
									jp_domain + "/api/widget").then(
									function(response) {
										$scope.shWidgets = response.data;
									}));
							$scope.postTypeSave = function() {
								var parameter = angular
										.toJson($scope.shPostType);
								$http
										.put(
												jp_domain + "/api/post/type/"
														+ $scope.postTypeId,
												parameter)
										.then(
												function(data, status, headers,
														config) {
													$state
															.go('content.post-type-item');
												},
												function(data, status, headers,
														config) {
													$state
															.go('content.post-type-item');
												});
							}
						} ]);

shioharaApp.controller('VecPostTypeAttrCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postTypeAttrId = $stateParams.postTypeAttrId;
			$scope.shPostTypeAttr = null;
			$rootScope.$state = $state;
			$scope.$evalAsync($http.get(
					jp_domain + "/api/post/type/attr/" + $scope.postTypeAttrId)
					.then(function(response) {
						$scope.shPostTypeAttr = response.data;
					}));
			$scope.postTypeAttrSave = function() {
				var parameter = angular.toJson($scope.shPostTypeAttr);
				$http.put(
						jp_domain + "/api/post/type/attr/"
								+ $scope.postTypeAttrId, parameter).then(
						function(data, status, headers, config) {
							$state.go('content.post-type-item');
						}, function(data, status, headers, config) {
							$state.go('content.post-type-item');
						});
			}

		} ]);

shioharaApp.controller('VecPostNewCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postTypeId = $stateParams.postTypeId;
			$scope.shPost = null;
			$scope.$evalAsync($http.get(
					jp_domain + "/api/post/type/" + $scope.postTypeId
							+ "/post/model").then(function(response) {
				$scope.shPost = response.data;
			}));
			$scope.postEditForm = "template/post/form.html";
			$scope.postSave = function() {
				var parameter = angular.toJson($scope.shPost);
				$http.post(jp_domain + "/api/post/" + $scope.postTypeId,
						parameter).then(
						function(data, status, headers, config) {
							$state.go('content');
						}, function(data, status, headers, config) {
							$state.go('content');
						});
			}
		} ]);

shioharaApp.controller('VecPostEditCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postId = $stateParams.postId;
			$scope.shPost = null;
			$scope.$evalAsync($http.get(
					jp_domain + "/api/post/" + $scope.postId).then(
					function(response) {
						$scope.shPost = response.data;
					}));
			$scope.postEditForm = "template/post/form.html";
			$scope.postSave = function() {
				var parameter = angular.toJson($scope.shPost);
				$http.put(jp_domain + "/api/post/" + $scope.postId, parameter)
						.then(function(data, status, headers, config) {
							$state.go('content');
						}, function(data, status, headers, config) {
							$state.go('content');
						});
			}
		} ]);

shioharaApp.controller('VecPostFormCtrl', [ "$scope", "$http", "$window",
		"$stateParams", "$state", "$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postTypeAttrId = $stateParams.postTypeAttrId;
			$scope.shPostTypeItem = angular.copy($scope.shPostType);

		} ]);
