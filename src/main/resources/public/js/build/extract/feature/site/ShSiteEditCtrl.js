shioharaApp.controller('ShSiteEditCtrl', [
		"$scope",
		"$state",
		"shSiteResource",
		"shSiteFactory",
		function($scope, shSiteResource, shSiteFactory) {
			$scope.shSite = shSiteResource.get({id: $stateParams.siteId});
			$scope.siteSave = function() {
				shSiteFactory.save($scope.shSite);
			}
			
			$scope.siteDelete = function() {
				shSiteFactory.delete($scope.shSite);
			}

		} ]);