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
			$scope.breadcrumb = null;
			$scope.shPost = shPostResource.get({
				id : $scope.postId
			}, function() {
				if ( $scope.shPost.shChannel != null) {
				$scope
				.$evalAsync($http
						.get(
								shAPIServerService
										.get()
										.concat(
												"/channel/" + $scope.shPost.shChannel.id + "/path")
												)
						.then(
								function(response) {
									$scope.breadcrumb = response.data.breadcrumb;
								}
								));
				}
			});
			
	
			
							
			$scope.openPreviewURL = function() {
				
				if ($scope.shPost.shChannel != null) {
				$scope
				.$evalAsync($http
						.get(
								shAPIServerService
										.get()
										.concat(
												"/channel/" + $scope.shPost.shChannel.id + "/path")
												)
						.then(
								function(response) {
									if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
										var previewURL = shAPIServerService.server().concat(
												"/sites/SampleSite/default/pt-br" + response.data.channelPath.replace(new RegExp(" ",
																'g'), "-"));
									}
									else {
									var previewURL = shAPIServerService.server().concat(
											"/sites/SampleSite/default/pt-br" + response.data.channelPath.replace(new RegExp(" ",
											'g'), "-")
													+ $scope.shPost.title.replace(new RegExp(" ",
															'g'), "-"));
									 
									}
									$window.open(previewURL,"_self");
								}));
				}
				else {
					if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
						var previewURL = shAPIServerService.server().concat(
								"/sites/SampleSite/default/pt-br" + response.data.channelPath.replace(new RegExp(" ",
												'g'), "-"));
					}
					else {
					   var previewURL = shAPIServerService.server().concat(
							"/sites/SampleSite/default/pt-br/"
									+ $scope.shPost.title.replace(new RegExp(" ",
											'g'), "-"));
					}
					 $window.open(previewURL,"_self");
				}
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