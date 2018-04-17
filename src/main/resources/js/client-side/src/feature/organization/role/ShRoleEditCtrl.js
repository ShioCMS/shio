shioharaApp.controller('ShRoleEditCtrl', [
	"$scope",
	"$http",
	"$window",
	"$stateParams",
	"$state",
	"$rootScope",
	"$translate",
	function ($scope, $http, $window, $stateParams, $state, $rootScope, $translate) {
		$rootScope.$state = $state;
		$scope.roleId = $stateParams.roleId;
		$scope.$evalAsync($http.get(
			"../api/role/" + $scope.roleId).then(
			function (response) {
				$scope.role = response.data;
			}));
		$scope.roleSave = function () {
			$scope.roles = null;
			var parameter = JSON.stringify($scope.role);
			$http.put("../api/role/" + $scope.roleId,
				parameter).then(
				function (data, status, headers, config) {
					  $state.go('organization.role');
				}, function (data, status, headers, config) {
					  $state.go('organization.role');
				});
		}
	}
]);