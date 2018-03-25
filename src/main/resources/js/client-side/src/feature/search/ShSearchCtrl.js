shioharaApp.controller('ShSearchCtrl', [
		"$scope",
		"$http",
		"$state",
		"$stateParams",
		"$location",
		"$rootScope",
		"shAPIServerService",
		function($scope, $http, $state, $stateParams, $location, $rootScope,
				shAPIServerService) {
			$scope.shQuery = $stateParams.query;
			$rootScope.$state = $state;
			$scope.shPosts = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/search?q=" + $scope.shQuery)).then(
					function(response) {
						$scope.shPostsWithBreadCrumb = response.data;
					}));
		} ]);
