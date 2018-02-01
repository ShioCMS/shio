shioharaApp.controller('ShSiteEditCtrl', [
		"$scope",
		"$http",
		"$state",
		"$stateParams",
		"$rootScope",
		"shSiteResource",
		"shAPIServerService",
		"Notification",
		function($scope, $http, $state, $stateParams, $rootScope,
				shSiteResource, shAPIServerService, Notification) {
			$scope.shSite = shSiteResource.get({id: $stateParams.siteId});
			$scope.siteSave = function() {
				$scope.shSite.$update(function() {
					Notification.warning('The ' + $scope.shSite.name +' Site was updated.');
					$state.go('content.children.site-children', {
						siteId : $scope.shSite.id
					});
				});
			}
			
			$scope.siteDelete = function() {
				shSiteResource
				.delete({
					id : $scope.shSite.id
				},function() {
					Notification.error('The ' + $scope.shSite.name +' Site was deleted.');
					$state.go('content',{}, {reload: true});
				});
			}

		} ]);