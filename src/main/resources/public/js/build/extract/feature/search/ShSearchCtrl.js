shioharaApp.controller('ShSearchCtrl', [
		"$scope",
		"$http",
		"$state",
		"$rootScope",
		"shAPIServerService",
		function($scope, $http, $state, $rootScope,shAPIServerService) {
			$rootScope.$state = $state;
			$scope.shPosts = null;
			$scope.q = "Index";
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/search?q=" + $scope.q)).then(
					function(response) {
						$scope.shPosts = response.data;
					}));
		} ]);