shioharaApp.controller('ShPostTypeSelectCtrl',
		[
				"$scope",
				"$http",
				"$window",
				"$state",
				"$rootScope",
				"shPostTypeResource",
				function($scope, $http, $window, $state, $rootScope,
						shPostTypeResource) {
					$rootScope.$state = $state;
					$scope.shPostTypes = shPostTypeResource.query();
				} ]);