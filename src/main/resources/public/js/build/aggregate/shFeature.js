shioharaApp.controller('ShContentCtrl', [
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
			$scope.$evalAsync($http.get(
					jp_domain + "/api/user/2?access_token="
							+ $scope.accessToken).then(function(response) {
				$scope.shUser = response.data;
			}));
			$scope.$evalAsync($http.get(jp_domain + "/api/post").then(
					function(response) {
						$scope.shPosts = response.data;
					}));
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
shioharaApp.controller('ShPostTypeSelectCtrl', [
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
				'ShPostTypeItemCtrl',
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
shioharaApp.controller('ShPostNewCtrl', [
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
shioharaApp.controller('ShPostEditCtrl', [
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
shioharaApp.controller('ShSiteListCtrl', [
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
