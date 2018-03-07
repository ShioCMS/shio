shioharaApp.controller('ShSiteEditCtrl', [
		"$scope",
		"$state",
		"$stateParams",
		"shSiteResource",
		"shSiteFactory",
		function($scope, $state, $stateParams, shSiteResource, shSiteFactory) {
			$scope.shSite = shSiteResource.get({id: $stateParams.siteId});
			$scope.siteSave = function() {
				shSiteFactory.save($scope.shSite);
			}
			
			$scope.siteDelete = function() {
				shSiteFactory.delete($scope.shSite);
			}

		} ]);