shioApp.controller('ShRoleCtrl', [
	"$scope",
	"$state",
	"$rootScope",
	"shRoleResource",
	"shRoleFactory",
	function ($scope, $state, $rootScope, shRoleResource, shRoleFactory) {
		$rootScope.$state = $state;
		$scope.roles = null;
		$scope.roles = shRoleResource.query();

		$scope.roleDelete = function (shRole) {
			shRoleFactory.delete(shRole);
		}
	}]);
