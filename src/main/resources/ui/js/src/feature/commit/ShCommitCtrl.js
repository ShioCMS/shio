shioharaApp.controller('ShCommitCtrl', [
	"$scope",
	"$http",
	"$rootScope",
	"$location",
	"shAPIServerService",
	"$state",
	"$window",
	"$stateParams",
	"vigLocale",
	"$translate",
	"shSiteResource",
	function ($scope, $http, $rootScope, $location, shAPIServerService,
		$state, $window, $stateParams, vigLocale, $translate,
		shSiteResource) {
		$scope.objectId = $stateParams.objectId;
		$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
		$translate.use($scope.vigLanguage);
		$scope.page = 0;
		$scope.shSite = shSiteResource.get({
			id: $stateParams.objectId
		});
		$scope.$evalAsync($http.get(
			shAPIServerService.get().concat(
				"/v2/history/object/" + $scope.objectId + "/0")).then(
					function (response) {
						$scope.commits = response.data
					}));

		$scope.objectPreview = function (shObject) {
			var link = shAPIServerService.get().concat(
				"/v2/object/" + shObject.id + "/preview");
			$window.open(link);
		}
		$scope.loadMore = function () {
			$scope.page = $scope.page + 1;
			$scope.$evalAsync($http.get(
				shAPIServerService.get().concat(
					"/v2/history/object/" + $scope.objectId + "/" + $scope.page)).then(
						function (response) {
							angular.forEach(response.data, function (commit, key) {
								$scope.commits.push(commit);
							});

						}));
		}
	}]);
