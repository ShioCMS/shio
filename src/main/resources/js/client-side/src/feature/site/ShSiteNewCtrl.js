shioharaApp.controller('ShSiteNewCtrl', [
		"$scope",
		"$http",
		"shAPIServerService",
		"shSiteFactory",
		function($scope, $http, shAPIServerService, shSiteFactory) {
			$scope.shSite = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/site/model")).then(
					function(response) {
						$scope.shSite = response.data;
					}));
			$scope.siteSave = function() {
				shSiteFactory.save($scope.shSite);
			}

		} ]);
