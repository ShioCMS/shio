shioharaApp.controller('ShPostTypeAttrCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postTypeAttrId = $stateParams.postTypeAttrId;
			$scope.shPostTypeAttr = null;
			$rootScope.$state = $state;
			$scope.$evalAsync($http.get(
					jp_domain + "/api/post/type/attr/" + $scope.postTypeAttrId)
					.then(function(response) {
						$scope.shPostTypeAttr = response.data;
					}));
			$scope.postTypeAttrSave = function() {
				var parameter = angular.toJson($scope.shPostTypeAttr);
				$http.put(
						jp_domain + "/api/post/type/attr/"
								+ $scope.postTypeAttrId, parameter).then(
						function(data, status, headers, config) {
							$state.go('content.post-type-item');
						}, function(data, status, headers, config) {
							$state.go('content.post-type-item');
						});
			}

		} ]);