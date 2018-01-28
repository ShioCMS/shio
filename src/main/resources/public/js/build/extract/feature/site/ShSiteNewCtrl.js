shioharaApp.controller('ShSiteNewCtrl', [
		"$scope",
		"$http",
		"$state",
		"$stateParams",
		"$rootScope",
		"shSiteResource",
		"shAPIServerService",
		function($scope, $http, $state, $stateParams, $rootScope, shSiteResource,
				shAPIServerService) {
			$scope.shSite = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/site/model")).then(
					function(response) {
						$scope.shSite = response.data;
					}));
			$scope.siteSave = function() {
				if ($scope.shSite.id != null && $scope.shSite.id > 0) {
					$scope.shSite.$update(function() {
						$state.go('content.children.site-children',{siteId: $scope.shSite.id});
					});
				} else {
					delete $scope.shSite.id;
					shSiteResource.save($scope.shSite, function(response){
						$state.go('content.children.site-children',{siteId: response.id});
					});
				}
			}

		} ]);