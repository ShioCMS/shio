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
			});

			$scope.openPreviewURL = function() {
				$scope
				.$evalAsync($http
						.get(
								shAPIServerService
										.get()
										.concat(
												"/channel/" + $scope.shPost.shChannel.id + "/path"))
						.then(
								function(response) {
									var previewURL = shAPIServerService.server().concat(
											"/sites/SampleSite/default/pt-br" + response.data.channelPath
													+ $scope.shPost.title.replace(new RegExp(" ",
															'g'), "-"));
									 $window.open(previewURL,"_self");
								}));
				}
				
	
			$scope.postEditForm = "template/post/form.html";
			$scope.postDelete = function() {
				shPostResource
				.delete({
					id : $scope.shPost.id
				},function() {
					$state.go('content',{}, {reload: true});
				});
			}
			$scope.postSave = function() {
				$scope.shPost.$update(function() {
					// $state.go('content');
				});
			}
		} ]);