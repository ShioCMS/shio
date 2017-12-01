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
