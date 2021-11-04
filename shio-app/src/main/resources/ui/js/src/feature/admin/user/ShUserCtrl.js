shioApp.controller('ShUserCtrl', [
	"$scope",
	"$state",
	"$rootScope",
	"shUserResource",
	"shUserFactory",
	function ($scope, $state, $rootScope, shUserResource, shUserFactory) {
		$rootScope.$state = $state;
		$scope.users = null;
		$scope.users = shUserResource.query();

		$scope.userDelete = function (shUser) {
			shUserFactory.delete(shUser);
		}
	}]);
