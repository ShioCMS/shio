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
						function($scope, $http, $window, $stateParams, $state,
								$rootScope, shAPIServerService, shPostResource,
								Notification, Upload, $q) {
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

								if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
									var previewURL = channelURL;
								} else {
									var previewURL = channelURL
											+ $scope.shPost.title.replace(
													new RegExp(" ", 'g'), "-");
								}
								$window.open(previewURL, "_self");
							}

							$scope.postSave = function() {

								// ********* FILE
								$scope.promise;
								$scope.deferred = $q.defer();
								$scope.promise = $scope.deferred.promise;
								$scope.file = $scope.shPost.shPostAttrs[0].strValue;
								$scope.filePath = null;
								if (!$scope.file.$error) {
									console.log("Inicio Upload");
									Upload
											.upload(
													{
														url : 'http://localhost:2710/api/staticfile/upload',
														data : {
															file : $scope.file,
															channelId : $scope.shChannel.id
														}
													})
											.then(
													function(resp) {
														console
																.log("Terminou Upload");
														$scope.filePath = resp.config.data.file.name;

														console.log("filePath:"
																+ $scope.filePath);
														$scope.shPost.shPostAttrs[0].strValue = $scope.filePath;
														console
																.log("$scope.shPost.shPostAttrs[0].strValue:"
																		+ $scope.shPost.shPostAttrs[0].strValue);
														$scope.deferred
																.resolve("teste");
														$timeout(function() {
															$scope.log = 'file: '
																	+ resp.config.data.file.name
																	+ ', Response: '
																	+ JSON
																			.stringify(resp.data)
																	+ '\n'
																	+ $scope.log;
														});
													},
													null,
													function(evt) {
														var progressPercentage = parseInt(100.0
																* evt.loaded
																/ evt.total);
														$scope.log = 'progress: '
																+ progressPercentage
																+ '% '
																+ evt.config.data.file.name
																+ '\n'
																+ $scope.log;
													});
								}

								// / *********** END FILE

								$scope.promise
										.then(function(dataThatWasPassed) {
											console.log("Comecou post");
											if ($scope.shPost.id != null) {
												$scope.shPost
														.$update(function() {
															console
																	.log("Terminou post");
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
																	console
																			.log(response);
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
