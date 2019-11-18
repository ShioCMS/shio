shioharaApp.controller('ShPostTypeEditorCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"shAPIServerService",
		"shPostTypeResource",
		"Notification",
		function($scope, $http, $window, $state, $rootScope,
				shAPIServerService, shPostTypeResource, Notification) {
			$rootScope.$state = $state;
			$scope.shPostType = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/v2/post/type/model")).then(
					function(response) {
						$scope.shPostType = response.data;
					}));
			$scope.postTypeSave = function() {
				delete $scope.shPostType.id;
				shPostTypeResource.save($scope.shPostType, function() {
					Notification.warning('The ' + $scope.shPostType.name +' Post Type was created.');
					$state.go('content.post-type-select');
				});
			}
		} ]);
