shioApp.controller('ShContentHomeCtrl', ["$http", "$scope",
	'vigLocale', "$translate", "shAPIServerService", "shSiteFactory",
	function ($http, $scope, vigLocale, $translate, shAPIServerService, shSiteFactory) {
		$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
		$translate.use($scope.vigLanguage);

		$scope.shWorkflowTasks = [];
		$scope.$evalAsync($http.get(shAPIServerService.get().concat(
			"/v2/workflow/task"))
			.then(
				function (response) {
					$scope.shWorkflowTasks = response.data;
				}));

		$scope.siteExport = function (shSite) {
			shSiteFactory.export(shSite);
		}
		$scope.siteNodeJS = function (shSite) {
			shSiteFactory.nodeJS(shSite);
		}
	}]);
