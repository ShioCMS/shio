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
						"shAPIServerService",
						'vigLocale',
						"shChannelFactory",
						"shPostFactory",
						"ShDialogSelectObject",
						function($scope, $state, $stateParams, $rootScope,
								$translate, $http, shAPIServerService,
								vigLocale, shChannelFactory, shPostFactory, ShDialogSelectObject) {

							$scope.siteId = $stateParams.siteId;
							$scope.channelId = $stateParams.channelId;
							$scope.$parent.channelId = $stateParams.channelId;
							$scope.vigLanguage = vigLocale.getLocale()
									.substring(0, 2);
							$translate.use($scope.vigLanguage);

							$scope.shSite = null;
							$scope.shChannels = null;
							$scope.shPosts = null;
							$scope.breadcrumb = null;
							$rootScope.$state = $state;
							$scope.shStateObjects = [];
							$scope.shObjects = [];

							$scope.$watch('$scope.shObjects', function() {
								console.log("modificou shObjects");
							});

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
								$scope.shSite = response.data.shSite;
								$scope.$parent.shSite = $scope.shSite;
								angular
										.forEach(
												$scope.shChannels,
												function(shChannel, key) {
													$scope.shStateObjects[shChannel.shGlobalId.id] = false;
													$scope.shObjects[shChannel.shGlobalId.id] = shChannel;
												});
								angular
										.forEach(
												$scope.shPosts,
												function(shPost, key) {
													$scope.shStateObjects[shPost.shGlobalId.id] = false;
													$scope.shObjects[shPost.shGlobalId.id] = shPost;
												});
							}
							
							
							$scope.objectsCopy = function() {
								for ( var stateKey in $scope.shStateObjects) {
									if ($scope.shStateObjects[stateKey] === true) {
										console.log("Copy " + stateKey);
									}
								}
							}
							
							$scope.objectsMove = function() {
								for ( var stateKey in $scope.shStateObjects) {
									if ($scope.shStateObjects[stateKey] === true) {
										console.log("Move " + stateKey);
										
										var modalInstance = ShDialogSelectObject.dialog($scope.channelId, "shChannel");
										modalInstance.result.then(function(shPostSelected) {
											// Selected Replace and Remove it
											$http.post(
													shAPIServerService.get().concat(
															"/reference/to/" + shObject.shGlobalId.id + "/replace/" + shPostSelected.shGlobalId.id))
													.then(function(response) {
														$ctrl.objectRefers = response.data;
													});
											
										}, function() {
											// Selected CANCEL
										});
									}
								}
							}
							
							$scope.objectsClone = function() {
								for ( var stateKey in $scope.shStateObjects) {
									if ($scope.shStateObjects[stateKey] === true) {
										console.log("Clone " + stateKey);
									}
								}
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
						} ]);