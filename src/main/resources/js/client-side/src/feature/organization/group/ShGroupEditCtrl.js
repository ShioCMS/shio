shioharaApp.controller('ShGroupEditCtrl', [
	"$scope",
	"$http",
	"$window",
	"$stateParams",
	"$state",
	"$rootScope",
	"$translate",
	function ($scope, $http, $window, $stateParams, $state, $rootScope, $translate) {
		$rootScope.$state = $state;
		$scope.groupId = $stateParams.groupId;
		$scope.$evalAsync($http.get(
			"../api/group/" + $scope.groupId).then(
			function (response) {
				$scope.group = response.data;
			}));
		$scope.groupSave = function () {
			$scope.groups = null;
			var parameter = JSON.stringify($scope.group);
			$http.put("../api/group/" + $scope.groupId,
				parameter).then(
				function (data, status, headers, config) {
					$state.go('organization.group');
				}, function (data, status, headers, config) {
					$state.go('organization.group');
				});
		}
	}
]);