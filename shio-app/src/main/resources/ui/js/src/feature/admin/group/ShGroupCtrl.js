shioApp.controller('ShGroupCtrl', [
	"$scope",
	"$state",
	"$rootScope",
	"shGroupResource",
	"shGroupFactory",
	function ($scope, $state, $rootScope, shGroupResource, shGroupFactory) {
		$rootScope.$state = $state;
		$scope.groups = null;
		$scope.groups = shGroupResource.query();

		$scope.groupDelete = function (shGroup) {
			shGroupFactory.delete(shGroup);
		}
	}]);
