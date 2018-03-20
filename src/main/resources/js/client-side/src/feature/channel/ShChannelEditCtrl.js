shioharaApp
		.controller(
				'ShChannelEditCtrl',
				[
						"$scope",
						"$http",
						"$window",
						"$state",
						"$stateParams",
						"$rootScope",
						"Token",
						"shChannelResource",
						"shSiteResource",
						"shAPIServerService",
						'vigLocale',
						'$location',
						'$translate',
						'Notification',
						function($scope, $http, $window, $state, $stateParams,
								$rootScope, Token, shChannelResource,
								shSiteResource, shAPIServerService, vigLocale,
								$location, $translate, Notification) {
							$scope.channelId = $stateParams.channelId;

							$scope.vigLanguage = vigLocale.getLocale()
									.substring(0, 2);
							$translate.use($scope.vigLanguage);
							$scope.shSite = null;
							$scope.shParentChannel = null;
							$scope.shChannel = null;
							$scope.breadcrumb = null;
							$rootScope.$state = $state;
							$scope.shChannel = shChannelResource.get({
								id : $scope.channelId
							});
							rootChannel = false;
							if ($scope.shChannel.rootChannel == 1) {
								rootChannel = true;
							}

							if (!rootChannel) {
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
															$scope.shParentChannel = response.data.currentChannel
															$scope.breadcrumb = response.data.breadcrumb;
															$scope.shSite = response.data.shSite;
														}));
							} else {
								$scope.shSite = $scope.shChannel.shSite;
							}
							$scope.channelSave = function() {
								$scope.shChannel
										.$update(function() {
											Notification.warning('The '
													+ $scope.shChannel.name
													+ ' Channel was updated.');
											$state
													.go(
															'content.children.channel-children',
															{
																channelId : $scope.shChannel.id
															});
										});
							}

						} ]);