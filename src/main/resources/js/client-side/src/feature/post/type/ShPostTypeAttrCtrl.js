shioharaApp.controller('ShPostTypeAttrCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shPostTypeAttrResource",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shPostTypeAttrResource) {
			$scope.postTypeAttrId = $stateParams.postTypeAttrId;
			$scope.shPostTypeAttr = shPostTypeAttrResource.get({
				id : $scope.postTypeAttrId
			});
			$rootScope.$state = $state;
			$scope.postTypeAttrSave = function() {
				$scope.shshPostTypeAttrPost.$update(function() {
					$state.go('content.post-type-item');
				});
			}

		} ]);