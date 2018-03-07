shioharaApp.controller('ShChannelChildrenCtrl', [
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
		function($scope, $state, $stateParams, $rootScope, $translate,$http,
				shAPIServerService, vigLocale, shChannelFactory, shPostFactory) {
			$scope.siteId = $stateParams.siteId;
			$scope.channelId = $stateParams.channelId;
			$scope.$parent.channelId = $stateParams.channelId;
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);

			$scope.shSite = null;
			$scope.shChannels = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;
			$rootScope.$state = $state;
		
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/channel/" + $scope.channelId + "/list")).then(
					function(response) {
						$scope.shChannels = response.data.shChannels;
						$scope.shPosts = response.data.shPosts;
						$scope.breadcrumb = response.data.breadcrumb;
						$scope.$parent.breadcrumb = response.data.breadcrumb;
						$scope.shSite = response.data.shSite;
						$scope.$parent.shSite = $scope.shSite;
					}));
			$scope.channelDelete = function(shChannel) {
				shChannelFactory.deleteFromList(shChannel, $scope.shChannels);
			}
			
			$scope.postDelete = function(shPost) {
				shPostFactory.deleteFromList(shPost, $scope.shPosts);
			}
		} ]);