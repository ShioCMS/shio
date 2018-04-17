shioharaApp.controller('ShRoleNewCtrl', [
	"$scope",
	"$http",
	"$window",
	"$state",
	"$rootScope",
	"$translate",
	function ($scope, $http, $window, $state, $rootScope, $translate) {
		$rootScope.$state = $state;
		$scope.role = {};
		$scope.roleSave = function () {
			var parameter = JSON.stringify($scope.role);
			$http.post("../api/role/",
				parameter).then(
				function (data, status, headers, config) {
					  $state.go('organization.role');
				}, function (data, status, headers, config) {
					  $state.go('organization.role');
				});
		}
	}
]);