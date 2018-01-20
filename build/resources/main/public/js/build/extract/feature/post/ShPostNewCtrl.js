shioharaApp
		.controller(
				'ShPostNewCtrl',
				[
						"$scope",
						"$http",
						"$window",
						"$stateParams",
						"$state",
						"$rootScope",
						"shAPIServerService",
						"shPostResource",
						function($scope, $http, $window, $stateParams, $state,
								$rootScope, shAPIServerService, shPostResource) {
							$scope.postTypeId = $stateParams.postTypeId;
							$scope.shPost = null;
							$scope.$evalAsync($http.get(
									shAPIServerService.get().concat(
											"/post/type/" + $scope.postTypeId
													+ "/post/model")).then(
									function(response) {
										$scope.shPost = response.data;
									}));
							$scope.postEditForm = "template/post/form.html";

							$scope.openPreviewURL = function() {

								if ($scope.shPost.shChannel != null) {
									$scope
									.$evalAsync($http
											.get(
													shAPIServerService
															.get()
															.concat(
																	"/channel/" + $scope.shPost.shChannel.id + "/path")
																	)
											.then(
													function(response) {
														if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
															var previewURL = shAPIServerService.server().concat(
																	"/sites/SampleSite/default/pt-br" + response.data.channelPath.replace(new RegExp(" ",
																					'g'), "-"));
														}
														else {
														var previewURL = shAPIServerService.server().concat(
																"/sites/SampleSite/default/pt-br" + response.data.channelPath.replace(new RegExp(" ",
																'g'), "-")
																		+ $scope.shPost.title.replace(new RegExp(" ",
																				'g'), "-"));
														 
														}
														$window.open(previewURL,"_self");
													}));
									}
									else {
										if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
											var previewURL = shAPIServerService.server().concat(
													"/sites/SampleSite/default/pt-br" + response.data.channelPath.replace(new RegExp(" ",
																	'g'), "-"));
										}
										else {
										   var previewURL = shAPIServerService.server().concat(
												"/sites/SampleSite/default/pt-br/"
														+ $scope.shPost.title.replace(new RegExp(" ",
																'g'), "-"));
										}
										 $window.open(previewURL,"_self");
									}
							}
							
							$scope.postSave = function() {
								if ($scope.shPost.id != null
										&& $scope.shPost.id > 0) {
									$scope.shPost.$update(function() {
										// $state.go('content');
									});
								} else {
									delete $scope.shPost.id;
									shPostResource.save($scope.shPost,
											function(response) {
												console.log(response);
												$scope.shPost = response;
											});
								}
							}
						} ]);
