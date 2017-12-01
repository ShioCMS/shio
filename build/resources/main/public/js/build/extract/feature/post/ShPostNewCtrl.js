shioharaApp.controller('ShPostNewCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postTypeId = $stateParams.postTypeId;
			$scope.shPost = null;
			$scope.$evalAsync($http.get(
					jp_domain + "/api/post/type/" + $scope.postTypeId
							+ "/post/model").then(function(response) {
				$scope.shPost = response.data;
			}));
			$scope.postEditForm = "template/post/form.html";
			$scope.postSave = function() {
				var parameter = angular.toJson($scope.shPost);
				$http.post(jp_domain + "/api/post/" + $scope.postTypeId,
						parameter).then(
						function(data, status, headers, config) {
							$state.go('content');
						}, function(data, status, headers, config) {
							$state.go('content');
						});
			}
		} ]);
