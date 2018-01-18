shioharaApp.controller('ShPostEditCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shPostResource",
		"shAPIServerService",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shPostResource, shAPIServerService) {
			$scope.postId = $stateParams.postId;

			$scope.shPost = shPostResource.get({
				id : $scope.postId
			}, function() {
				$scope.previewURL = shAPIServerService.server().concat(
						"/sites/SampleSite/default/pt-br/"
								+ $scope.shPost.title.replace(new RegExp(" ",
										'g'), "-"));
			});

			$scope.postEditForm = "template/post/form.html";
			$scope.postSave = function() {
				$scope.shPost.$update(function() {
					//$state.go('content');
				});
			}
		} ]);