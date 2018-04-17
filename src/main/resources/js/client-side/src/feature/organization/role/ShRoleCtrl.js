shioharaApp.controller('ShRoleCtrl', [
	"$scope",
	"$http",
	"$window",
	"$state",
	"$rootScope",
	"$translate",
	function ($scope, $http, $window, $state, $rootScope, $translate) {
		$rootScope.$state = $state;
		$scope.roles = null;

		$scope.$evalAsync($http.get(
			"../api/role/").then(
			function (response) {
				$scope.roles = response.data;
			}));

		$scope.roleDelete = function (roleId) {
			$http.delete("../api/role/" + roleId).then(
				function (data, status, headers, config) {
					$http.get(
						"../api/role/").then(
						function (response) {
							$scope.roles = response.data;
						});
				}, function (data, status, headers, config) {
					// called asynchronously if an error occurs
					// or server returns response with an error
					// status.
				});
		}
	}]);