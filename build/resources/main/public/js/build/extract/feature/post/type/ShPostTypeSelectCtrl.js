shioharaApp.controller('ShPostTypeSelectCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $state, $rootScope) {
			$scope.shPostTypes = null;
			$rootScope.$state = $state;
			$scope.$evalAsync($http.get(jp_domain + "/api/post/type").then(
					function(response) {
						$scope.shPostTypes = response.data;
					}));
		} ]);