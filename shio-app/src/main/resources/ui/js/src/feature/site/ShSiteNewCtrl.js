shioApp.controller('ShSiteNewCtrl', [
	"$scope",
	"$http",
	"shAPIServerService",
	"shSiteFactory",
	"$state",
	function ($scope, $http, shAPIServerService, shSiteFactory, $state) {
		$scope.shSite = null;
		$scope.$evalAsync($http.get(
			shAPIServerService.get().concat("/v2/site/model")).then(
				function (response) {
					$scope.shSite = response.data;
				}));
		$scope.siteSave = function () {
			shSiteFactory.save($scope.shSite);

		}

	}]);
