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
						"Notification",
						"Upload",
						"$q",
						"$timeout",
						"shStaticFileFactory",
						function($scope, $http, $window, $stateParams, $state,
								$rootScope, shAPIServerService, shPostResource,
								Notification, Upload, $q, $timeout,
								shStaticFileFactory) {
							$scope.tinymceOptions = {
								plugins : 'link image code',
								toolbar : 'undo redo | bold italic | alignleft aligncenter alignright | code'
							};
							$scope.channelId = $stateParams.channelId;
							$scope.postTypeId = $stateParams.postTypeId;
							$scope.breadcrumb = null;
							$scope.shPost = null;
							$scope.shChannel = null;
							$scope.shSite = null;
							var channelURL = null;
							$scope
									.$evalAsync($http
											.get(
													shAPIServerService
															.get()
															.concat(
																	"/channel/"
																			+ $scope.channelId
																			+ "/path"))
											.then(
													function(response) {
														$scope.shChannel = response.data.currentChannel
														$scope.breadcrumb = response.data.breadcrumb;
														$scope.shSite = response.data.shSite;
														folderPath = shAPIServerService
																.server()
																.concat(
																		"/store/file_source/"
																				+ $scope.shSite.name
																				+ response.data.channelPath);
														channelURL = shAPIServerService
																.server()
																.concat(
																		"/sites/"
																				+ $scope.shSite.name
																						.replace(
																								new RegExp(
																										" ",
																										'g'),
																								"-")
																				+ "/default/pt-br"
																				+ response.data.channelPath
																						.replace(
																								new RegExp(
																										" ",
																										'g'),
																								"-"));
													}));
							$scope.$evalAsync($http.get(
									shAPIServerService.get().concat(
											"/post/type/" + $scope.postTypeId
													+ "/post/model")).then(
									function(response) {
										$scope.shPost = response.data;
									}));
							$scope.postEditForm = "template/post/form.html";

							$scope.openPreviewURL = function() {
								if ($scope.shPost.shPostType.name == 'PT-FILE') {
									var previewURL = folderPath
											+ $scope.shPost.title;
								} else if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
									var previewURL = channelURL;
								} else {
									var previewURL = channelURL
											+ $scope.shPost.title.replace(
													new RegExp(" ", 'g'), "-");
								}
								$window.open(previewURL, "_self");
							}

							var uploadFile = function(shPostAttr, key, postType) {
								return shStaticFileFactory.uploadFile(
										$scope.channelId, shPostAttr, key,
										postType, $scope.shPost,
										$scope.numberOfFileWidgets);
							}
							$scope.postSave = function() {

								var promiseFiles = [];

								$scope.filePath = null;
								$scope.numberOfFileWidgets = 0;
								var postType = $scope.shPost.shPostType;
								angular
										.forEach($scope.shPost.shPostAttrs,
												function(shPostAttr, key) {
													promiseFiles
															.push(uploadFile(
																	shPostAttr,
																	key,
																	postType));
												});

								$q
										.all(promiseFiles)
										.then(
												function(dataThatWasPassed) {
													if ($scope.shPost.id != null) {
														$scope.shPost
																.$update(function() {
																	Notification
																			.warning('The '
																					+ $scope.shPost.title
																					+ ' Post was update.');
																	// $state.go('content');
																});
													} else {
														delete $scope.shPost.id;
														$scope.shPost.shChannel = $scope.shChannel;
														shPostResource
																.save(
																		$scope.shPost,
																		function(
																				response) {
																			$scope.shPost = response;
																			Notification
																					.warning('The '
																							+ $scope.shPost.title
																							+ ' Post was created.');
																		});
													}

												});
							}

						} ]);
