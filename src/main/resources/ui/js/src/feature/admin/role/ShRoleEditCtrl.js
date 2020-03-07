shioApp.controller('ShRoleEditCtrl', [
	"$scope",
	"$state",
	"$rootScope",
	"shRoleResource",
	"$stateParams",
	"Notification",
	function ($scope, $state, $rootScope,
		shRoleResource, $stateParams, Notification) {
		$rootScope.$state = $state;
		$scope.roleId = $stateParams.roleId;

		$scope.role = shRoleResource.get({
			id: $scope.roleId
		});

		$scope.roleSave = function () {
			$scope.role.$update(function () {
				Notification.warning('The ' + $scope.role.name + ' Role was updated.');
			});
		}
	}]);