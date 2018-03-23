shioharaApp
		.controller(
				'ShChannelChildrenCtrl',
				[
						"$scope",
						"$state",
						"$stateParams",
						"$rootScope",
						"$translate",
						"$http",
						"$window",
						"shAPIServerService",
						'vigLocale',
						"shChannelFactory",
						"shPostFactory",
						"ShDialogSelectObject",
						"$filter",
						"Notification",
						function($scope, $state, $stateParams, $rootScope,
								$translate, $http, $window,
								shAPIServerService, vigLocale,
								shChannelFactory, shPostFactory,
								ShDialogSelectObject, $filter, Notification) {

							$scope.siteId = $stateParams.siteId;
							$scope.channelId = $stateParams.channelId;
							$scope.$parent.channelId = $stateParams.channelId;
							$scope.vigLanguage = vigLocale.getLocale()
									.substring(0, 2);
							$translate.use($scope.vigLanguage);

							$scope.shCurrentChannel = null;
							$scope.shSite = null;
							$scope.shChannels = null;
							$scope.shPosts = null;
							$scope.breadcrumb = null;
							$rootScope.$state = $state;
							$scope.shStateObjects = [];
							$scope.shObjects = [];
							$scope.actions = [];

							$scope.$evalAsync($http.get(
									shAPIServerService.get().concat(
											"/channel/" + $scope.channelId
													+ "/list")).then(
									function(response) {
										$scope.processResponse(response);
									}));

							$scope.processResponse = function(response) {
								$scope.shChannels = response.data.shChannels;
								$scope.shPosts = response.data.shPosts;
								$scope.breadcrumb = response.data.breadcrumb;
								$scope.$parent.breadcrumb = response.data.breadcrumb;
								$scope.shCurrentChannel = $scope.breadcrumb
										.slice(-1).pop();
								$scope.shSite = response.data.shSite;
								$scope.$parent.shSite = $scope.shSite;
								angular
										.forEach(
												$scope.shChannels,
												function(shChannel, key) {
													$scope.shStateObjects[shChannel.shGlobalId.id] = false;
													$scope.shObjects[shChannel.shGlobalId.id] = shChannel;
													$scope.actions[shChannel.shGlobalId.id] = false;
												});
								angular
										.forEach(
												$scope.shPosts,
												function(shPost, key) {
													$scope.shStateObjects[shPost.shGlobalId.id] = false;
													$scope.shObjects[shPost.shGlobalId.id] = shPost;
													$scope.actions[shPost.shGlobalId.id] = false;
												});
							}

							$scope.updateAction = function(shGlobalId, value) {
								$scope.actions[shGlobalId.id] = value;
							}
							$scope.objectsCopy = function() {
								var modalInstance = ShDialogSelectObject
										.dialog($scope.channelId, "shChannel");
								var objectGlobalIds = [];
								for ( var stateKey in $scope.shStateObjects) {
									if ($scope.shStateObjects[stateKey] === true) {
										var objectGlobalId = "" + stateKey;
										objectGlobalIds.push(objectGlobalId);
									}
								}
								modalInstance.result
										.then(
												function(shObjectSelected) {
													var parameter = JSON
															.stringify(objectGlobalIds);
													$http
															.put(
																	shAPIServerService
																			.get()
																			.concat(
																					"/post/copyto/"
																							+ shObjectSelected.shGlobalId.id),
																	parameter)
															.then(
																	function(
																			response) {
																		var shMovedPosts = response.data;
																		for (i = 0; i < shMovedPosts.length; i++) {
																			shMovedPost = shMovedPosts[i];
																			var copiedMessage = 'The '
																					+ shMovedPost.title
																					+ ' Post was copied.';
																			Notification
																					.warning(copiedMessage);
																		}
																	});

												}, function() {
													// Selected CANCEL
												});

							}

							$scope.objectsMove = function() {
								var modalInstance = ShDialogSelectObject
										.dialog($scope.channelId, "shChannel");
								var objectGlobalIds = [];
								for ( var stateKey in $scope.shStateObjects) {
									if ($scope.shStateObjects[stateKey] === true) {
										var objectGlobalId = "" + stateKey;
										objectGlobalIds.push(objectGlobalId);
									}
								}
								modalInstance.result
										.then(
												function(shObjectSelected) {
													var parameter = JSON
															.stringify(objectGlobalIds);
													$http
															.put(
																	shAPIServerService
																			.get()
																			.concat(
																					"/post/moveto/"
																							+ shObjectSelected.shGlobalId.id),
																	parameter)
															.then(
																	function(
																			response) {
																		var shMovedPosts = response.data;
																		for (i = 0; i < shMovedPosts.length; i++) {
																			shMovedPost = shMovedPosts[i];
																			var movedMessage = 'The '
																					+ shMovedPost.title
																					+ ' Post was moved.';
																			// filter
																			// the
																			// array
																			var foundItem = $filter(
																					'filter')
																					(
																							$scope.shPosts,
																							{
																								id : shMovedPost.id
																							},
																							true)[0];
																			// get
																			// the
																			// index
																			var index = $scope.shPosts
																					.indexOf(foundItem);
																			// remove
																			// the
																			// item
																			// from
																			// array
																			$scope.shPosts
																					.splice(
																							index,
																							1);
																			Notification
																					.warning(movedMessage);
																		}
																	});

												}, function() {
													// Selected CANCEL
												});

							}

							$scope.objectsClone = function() {
								var objectGlobalIds = [];
								for ( var stateKey in $scope.shStateObjects) {
									if ($scope.shStateObjects[stateKey] === true) {
										var objectGlobalId = "" + stateKey;
										objectGlobalIds.push(objectGlobalId);
									}
								}
								var parameter = JSON.stringify(objectGlobalIds);
								$http
										.put(
												shAPIServerService
														.get()
														.concat(
																"/post/copyto/"
																		+ $scope.shCurrentChannel.shGlobalId.id),
												parameter)
										.then(
												function(response) {
													var shClonedPosts = response.data;
													for (i = 0; i < shClonedPosts.length; i++) {
														shClonedPost = shClonedPosts[i];
														var clonedMessage = 'The '
																+ shClonedPost.title
																+ ' Post was cloned.';
														Notification
																.warning(clonedMessage);
													}
												});

							}

							$scope.objectsRename = function() {
								for ( var stateKey in $scope.shStateObjects) {
									if ($scope.shStateObjects[stateKey] === true) {
										console.log("Rename " + stateKey);
									}
								}
							}

							$scope.objectsEdit = function() {
								for ( var stateKey in $scope.shStateObjects) {
									if ($scope.shStateObjects[stateKey] === true) {
										console.log("Edit " + stateKey);
									}
								}
							}
							$scope.objectsDelete = function() {
								for ( var stateKey in $scope.shStateObjects) {
									if ($scope.shStateObjects[stateKey] === true) {
										shPostFactory.deleteFromList(
												$scope.shObjects[stateKey],
												$scope.shPosts);
									}
								}
							}

							$scope.channelDelete = function(shChannel) {
								shChannelFactory.deleteFromList(shChannel,
										$scope.shChannels);
							}

							$scope.postDelete = function(shPost) {
								shPostFactory.deleteFromList(shPost,
										$scope.shPosts);
							}
							$scope.postPreview = function(shPost) {
								var link = shAPIServerService.get().concat(
										"/object/" + shPost.shGlobalId.id
												+ "/preview");
								$window.open(link);
							}
							$scope.channelPreview = function(shChannel) {
								var link = shAPIServerService.get().concat(
										"/object/" + shChannel.shGlobalId.id
												+ "/preview");
								$window.open(link);
							}
						} ]);