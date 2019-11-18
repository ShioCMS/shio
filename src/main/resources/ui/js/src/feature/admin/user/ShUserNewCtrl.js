shioharaApp.controller('ShUserNewCtrl', [
		"$scope",
		"$http",
		"$state",
		"$rootScope",
		"shUserFactory",
		"shAPIServerService",
		function($scope, $http, $state, $rootScope,
				shUserFactory, shAPIServerService) {
			$rootScope.$state = $state;
			$scope.user = {};
			$scope.isNew = true;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/v2/user/model")).then(
					function(response) {
						$scope.user = response.data;
					}));
			$scope.userSave = function() {
				shUserFactory.save($scope.user, $scope.isNew);
			}
		} ]);