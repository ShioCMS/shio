shioharaApp.controller('ShPostTypeSelectCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$stateParams",
		"$rootScope",
		"shPostTypeResource",
		"shSiteResource",
		"shAPIServerService",
		function($scope, $http, $window, $state, $stateParams, $rootScope,
				shPostTypeResource, shSiteResource, shAPIServerService) {
			$scope.siteId = $stateParams.siteId;
			$scope.channelId = $stateParams.channelId;
			$rootScope.$state = $state;
			$scope.shPostTypes = shPostTypeResource.query();
			$scope.shSite = null;
			$scope.breadcrumb = null;
			if ($scope.channelId != null) {
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/channel/" + $scope.channelId + "/path"))
						.then(function(response) {
							$scope.breadcrumb = response.data.breadcrumb;
							$scope.shSite = response.data.currentChannel.shSite;
						}));
			} else {
				$scope.shSite = shSiteResource.get({
					id : $scope.siteId
				});
			}
		} ]);