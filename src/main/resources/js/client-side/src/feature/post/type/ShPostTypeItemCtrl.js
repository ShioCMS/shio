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
						"Notification",
						function($scope, $http, $window, $stateParams, $state,
								$rootScope, shWidgetResource,
								shPostTypeResource, shPostTypeAttrResource,
								shAPIServerService, Notification) {
							$scope.postTypeId = $stateParams.postTypeId;
							$scope.shPostType = null;
							$scope.shWidgets = shWidgetResource.query();
							$rootScope.$state = $state;
							
						    $scope.sortableOptions = {
						            handle: ' .handle'
						            // items: ' .panel:not(.panel-heading)'
						            // axis: 'y'
						        }
						    
							$scope.shPostType = shPostTypeResource
									.get(
											{
												id : $scope.postTypeId
											},
											function(response) {
												$scope.shPostNewItem = angular
														.copy($scope.shPostType);												
											});

							$scope.shPostTypeAttrModel = null;

							$scope
									.$evalAsync($http
											.get(
													shAPIServerService
															.get()
															.concat(
																	"/v2/post/type/attr/model"))
											.then(
													function(response) {
														$scope.shPostTypeAttrModel = response.data;
													}));

							$scope.postTypeSaveAndClose = function() {
								$scope.shPostType.$update(function() {
									Notification.warning('The ' + $scope.shPostType.name +' Site was updated.');
									$state.go('content.post-type-select');
								});

							}
							
							$scope.postTypeSave = function() {
								$scope.shPostType.$update(function() {
									Notification.warning('The ' + $scope.shPostType.name +' Site was updated.');							
								});

							}
							$scope.removePostType = function() {
								shPostTypeResource
								.delete({
									id : $scope.shPostType.id
								},function() {
									Notification.error('The ' + $scope.shPostType.name +' Site was deleted.');
									$state.go('content.post-type-select');
								});
							}
							
							$scope.addPostTypeAttr = function(shWidget, shPostTypeAttrs) {
								console.log("addPostTypeAttr");
								$scope.shPostTypeAttrModel.shWidget = shWidget;
								
								$scope.shPostTypeAttrModel.ordinal = shPostTypeAttrs.length;
								$scope.shPostTypeAttrModel.label = "Field " + (shPostTypeAttrs.length + 1);
								$scope.shPostTypeAttrModel.name = "FIELD_" + (shPostTypeAttrs.length + 1);	
								delete $scope.shPostTypeAttrModel.id;
								shPostTypeAttrs.push(angular
										.copy($scope.shPostTypeAttrModel));
							}
							
							$scope.postTypeAttrDelete = function(shPostTypeAttrs, shPostTypeAttr,index) {
								console.log("postTypeAttrDelete");
						
									var index = shPostTypeAttrs
											.indexOf(shPostTypeAttr);
									shPostTypeAttrs.splice(
											index, 1);
						
							}
						} ]);
