shioharaApp.controller('ShPostNewCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shAPIServerService",
		function($scope, $http, $window, $stateParams, $state, $rootScope, shAPIServerService) {
			$scope.postTypeId = $stateParams.postTypeId;
			$scope.shPost = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/api/post/type/" + $scope.postTypeId
							+ "/post/model").then(function(response) {
				$scope.shPost = response.data;
			})));
			$scope.postEditForm = "template/post/form.html";
			$scope.postSave = function() {
				$scope.shPost.$update(function() {
					$state.go('content');
				});
			}
		} ]);
