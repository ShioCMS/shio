shPreviewApp.controller('ShPreviewCtrl', [
		"$scope",
		"$state",
		"$stateParams",
		"$sce",
		"shAPIServerService",
		"$http",
		"$window",
		function($scope, $state, $stateParams, $sce, shAPIServerService, $http,
				$window) {
			$scope.objectId = $stateParams.objectId;
			$scope.objectInfo = null;
			$scope.actionButton = null;
			$scope.returnButton = null;
			$scope.captionButton = "Loading...";
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/v2/object/" + $scope.objectId + "/path")).then(
					function(response) {
						$scope.objectInfo = response.data;
						if ($scope.objectInfo.shPost == null) {
							if ($scope.objectInfo.currentFolder == null) {
								if ($scope.objectInfo.shSite != null) {
									$scope.captionButton = "Edit the Site";
									$scope.actionButton = "/content#!/site/"
											+ $scope.objectId + "/edit";
									$scope.returnButton = "/content#!/list/"
											+ $scope.objectId;

								}
							} else {
								$scope.captionButton = "Edit the Folder";
								$scope.actionButton = "/content#!/folder/"
										+ $scope.objectId + "/edit";
								
								if ($scope.objectInfo.currentFolder.parentFolder != null) {
								$scope.returnButton = "/content#!/list/"
										+  $scope.objectInfo.currentFolder.parentFolder.id;
								}
								else {
									$scope.returnButton = "/content#!/list/"
										+ $scope.objectInfo.shSite.id;
								}
							}
						} else {
							$scope.captionButton = "Edit the Post";
							$scope.actionButton = "/content#!/post/type/"
									+ $scope.objectInfo.shPost.shPostType.id
									+ "/post/" + $scope.objectId;
							$scope.returnButton = "/content#!/list/"
									+ $scope.objectInfo.shPost.shFolder.id;
						}
					}));
			$scope.previewURL = $sce
					.trustAsResourceUrl(shAPIServerService.get().concat(
							"/v2/object/" + $scope.objectId + "/preview"));
			$scope.isMobile = false;

			$scope.actionLink = function() {
				$window.open($scope.actionButton, "_self");
			}
			$scope.returnLink = function() {
				$window.open($scope.returnButton, "_self");
			}

		} ]);