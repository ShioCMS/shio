shioharaApp.controller('ShSearchTypeCtrl', [
		"$scope",
		"$http",
		"$state",
		"$stateParams",
		"$location",
		"$rootScope",
		"shAPIServerService",
		function($scope, $http, $state, $stateParams, $location, $rootScope,
				shAPIServerService) {
			$scope.type = $stateParams.type;
			$rootScope.$state = $state;
			$scope.shPosts = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/v2/search/type/" + $scope.type)).then(
					function(response) {
						$scope.shPostsWithBreadCrumb = response.data;
					}));
		} ]);
