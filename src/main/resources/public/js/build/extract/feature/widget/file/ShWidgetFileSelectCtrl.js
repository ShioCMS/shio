shioharaApp.controller('ShWidgetFileSelectCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		function($scope, shAPIServerService, $http) {
			$scope.channelId = "83041422-e6d9-444f-bb82-2a6dbb9ba3c2";
			$scope.shSite = null;
			$scope.shChannels = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;

			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/channel/" + $scope.channelId + "/list")).then(
					function(response) {
						$scope.shChannels = response.data.shChannels;
						$scope.shPosts = response.data.shPosts;
						$scope.breadcrumb = response.data.breadcrumb;
						$scope.shSite = response.data.shSite;
					}));
		} ]);