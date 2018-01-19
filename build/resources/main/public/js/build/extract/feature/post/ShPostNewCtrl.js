shioharaApp.controller('ShPostNewCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shAPIServerService",
		"shPostResource",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shAPIServerService, shPostResource) {
			$scope.postTypeId = $stateParams.postTypeId;
			$scope.shPost = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/post/type/" + $scope.postTypeId + "/post/model"))
					.then(function(response) {
						$scope.shPost = response.data;
					}));
			$scope.postEditForm = "template/post/form.html";
			
			$scope.openPreviewURL = function() {
				var previewURL = shAPIServerService.server().concat(
						"/sites/SampleSite/default/pt-br/"
								+ $scope.shPost.title.replace(new RegExp(" ",
										'g'), "-"));
				 $window.open(previewURL,"_self");

			}
			
			$scope.postSave = function() {
				if ($scope.shPost.id != null && $scope.shPost.id > 0) {
					$scope.shPost.$update(function() {
						// $state.go('content');
					});
				} else {
					delete $scope.shPost.id;
					shPostResource.save($scope.shPost, function(response) {
						console.log(response);
						$scope.shPost = response;
					});
				}
			}
		} ]);
