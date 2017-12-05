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
						function($scope, $http, $window, $stateParams, $state,
								$rootScope, shWidgetResource, shPostTypeResource) {
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

							$scope.postTypeSave = function() {
								$scope.shPostType.$update(function() {
									$state.go('content.post-type-item');
								});
							}
						} ]);
