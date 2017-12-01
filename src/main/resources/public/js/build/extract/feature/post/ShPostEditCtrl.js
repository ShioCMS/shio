shioharaApp.controller('ShPostEditCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postId = $stateParams.postId;
			$scope.shPost = null;
			$scope.$evalAsync($http.get(
					jp_domain + "/api/post/" + $scope.postId).then(
					function(response) {
						$scope.shPost = response.data;
					}));
			$scope.postEditForm = "template/post/form.html";
			$scope.postSave = function() {
				var parameter = angular.toJson($scope.shPost);
				$http.put(jp_domain + "/api/post/" + $scope.postId, parameter)
						.then(function(data, status, headers, config) {
							$state.go('content');
						}, function(data, status, headers, config) {
							$state.go('content');
						});
			}
		} ]);