shioharaApp.controller('ShChannelNewCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$stateParams",
		"$rootScope",
		"Token",
		"shChannelResource",
		"shAPIServerService",
		'vigLocale',
		'$location',
		"$translate",
		function($scope, $http, $window, $state, $stateParams, $rootScope,
				Token, shChannelResource, shAPIServerService, vigLocale, $location,
				$translate, breadcrumb) {
			$scope.siteId = $stateParams.siteId;
			$scope.channelId = $stateParams.channelId;
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);

			$scope.shParentChannel = null;
			$scope.shChannel = null;
			$scope.breadcrumb = null;
			$rootScope.$state = $state;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/channel/model")).then(
					function(response) {
						$scope.shChannel = response.data;
					}));
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/channel/" + $scope.channelId + "/path")).then(
					function(response) {
						$scope.shParentChannel = response.data.currentChannel
						$scope.breadcrumb = response.data.breadcrumb;
						$scope.$parent.breadcrumb = response.data.breadcrumb;
					}));

			$scope.channelSave = function() {
				if ($scope.shChannel.id != null && $scope.shChannel.id > 0) {
					$scope.shChannel.$update(function() {
						// $state.go('content');
					});
				} else {
					delete $scope.shChannel.id;
					$scope.shChannel.parentChannel = $scope.shParentChannel;
					shChannelResource.save($scope.shChannel, function(response) {
						console.log(response);
						$scope.shChannel = response;
					});
				}
			}

		} ]);