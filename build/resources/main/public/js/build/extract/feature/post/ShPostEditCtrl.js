shioharaApp.controller('ShPostEditCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shPostResource",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shPostResource) {
			$scope.postId = $stateParams.postId;

			$scope.shPost = shPostResource.get({
				id : $scope.postId
			});

			$scope.postEditForm = "template/post/form.html";
			
			$scope.postSave = function() {
				$scope.shPost.$update(function() {
					$state.go('content');
				});
			}
		} ]);