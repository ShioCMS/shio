shioharaApp.controller('ShSiteNewCtrl', [
		"$scope",
		"$http",
		"$state",
		"$stateParams",
		"$rootScope",
		"shSiteResource",
		"shAPIServerService",
		"Notification",
		function($scope, $http, $state, $stateParams, $rootScope, shSiteResource,
				shAPIServerService, Notification) {
			$scope.shSite = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/site/model")).then(
					function(response) {
						$scope.shSite = response.data;
					}));
			$scope.siteSave = function() {
				if ($scope.shSite.id != null && $scope.shSite.id > 0) {
					$scope.shSite.$update(function() {
						Notification.warning('The ' + $scope.shSite.name +' Site was updated.');
						$state.go('content.children.site-children',{siteId: $scope.shSite.id});						
					});
				} else {
					delete $scope.shSite.id;
					shSiteResource.save($scope.shSite, function(response){
						Notification.warning('The ' + $scope.shSite.name +' Site was created.');
						$state.go('content.children.site-children',{siteId: response.id});
					});
				}
			}

		} ]);