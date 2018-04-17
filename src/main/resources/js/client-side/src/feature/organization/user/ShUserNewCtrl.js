shioharaApp.controller('ShUserNewCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"$translate",
		"shUserFactory",
		"shAPIServerService",
		function($scope, $http, $window, $state, $rootScope, $translate,
				shUserFactory, shAPIServerService) {
			$rootScope.$state = $state;
			$scope.user = {};
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/v2/user/model")).then(
					function(response) {
						$scope.user = response.data;
					}));
			$scope.userSave = function() {
				shUserFactory.save($scope.user);
			}
		} ]);