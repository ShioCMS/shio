shioharaApp.controller('ShPostNewCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shAPIServerService",
		"shPostResource",
		"Notification",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shAPIServerService, shPostResource, Notification) {
			$scope.channelId = $stateParams.channelId;
			$scope.postTypeId = $stateParams.postTypeId;
			$scope.breadcrumb = null;
			$scope.shPost = null;
			$scope.shChannel = null;
			$scope.shSite = null;
			var channelURL = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/channel/" + $scope.channelId + "/path")).then(
					function(response) {
						$scope.shChannel = response.data.currentChannel
						$scope.breadcrumb = response.data.breadcrumb;
						$scope.shSite = response.data.shSite;
						channelURL = shAPIServerService.server().concat(
								"/sites/"
										+ $scope.shSite.name.replace(new RegExp(" ",
										'g'), "-")
										+ "/default/pt-br"
										+ response.data.channelPath.replace(
												new RegExp(" ", 'g'), "-"));
					}));
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/post/type/" + $scope.postTypeId + "/post/model"))
					.then(function(response) {
						$scope.shPost = response.data;
					}));
			$scope.postEditForm = "template/post/form.html";

			$scope.openPreviewURL = function() {

				if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
					var previewURL = channelURL;
				} else {
					var previewURL = channelURL
							+ $scope.shPost.title.replace(new RegExp(" ", 'g'),
									"-");
				}
				$window.open(previewURL, "_self");
			}

			$scope.postSave = function() {
				if ($scope.shPost.id != null && $scope.shPost.id > 0) {
					$scope.shPost.$update(function() {
						Notification.warning('The '
								+ $scope.shPost.title
								+ ' Post was update.');
						// $state.go('content');
					});
				} else {
					delete $scope.shPost.id;
					$scope.shPost.shChannel = $scope.shChannel;
					shPostResource.save($scope.shPost, function(response) {
						console.log(response);
						$scope.shPost = response;
						Notification.warning('The '
								+ $scope.shPost.title
								+ ' Post was created.');
					});
				}
			}
		} ]);
