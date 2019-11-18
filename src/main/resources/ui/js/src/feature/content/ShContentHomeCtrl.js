shioharaApp.controller('ShContentHomeCtrl', ["$http", "$scope",
	'vigLocale', "$translate", "shAPIServerService",
	function ($http, $scope, vigLocale, $translate, shAPIServerService) {
		$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
		$translate.use($scope.vigLanguage);

		$scope.shWorkflowTasks = [];
		$scope.$evalAsync($http.get(shAPIServerService.get().concat(
			"/v2/workflow/task"))
			.then(
				function (response) {
					$scope.shWorkflowTasks = response.data;
				}));
	}]);
