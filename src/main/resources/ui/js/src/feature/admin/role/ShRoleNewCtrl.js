shioharaApp.controller('ShRoleNewCtrl', [
	"$scope",
	"$http",
	"$state",
	"$rootScope",
	"shRoleFactory",
	"shAPIServerService",
	function($scope, $http, $state, $rootScope,
			shRoleFactory, shAPIServerService) {
		$rootScope.$state = $state;
		$scope.role = {};
		$scope.$evalAsync($http.get(
				shAPIServerService.get().concat("/v2/role/model")).then(
				function(response) {
					$scope.role = response.data;
				}));
		$scope.roleSave = function() {
			shRoleFactory.save($scope.role);
		}
	} ]);