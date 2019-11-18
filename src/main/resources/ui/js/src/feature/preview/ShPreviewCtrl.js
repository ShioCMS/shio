shPreviewApp.controller('ShPreviewCtrl', [
	"$scope",
	"$state",
	"$stateParams",
	"$sce",
	"shAPIServerService",
	"$http",
	"$window",
	"$timeout",
	"$document",
	function ($scope, $state, $stateParams, $sce, shAPIServerService, $http,
		$window, $timeout, $document) {
		angular.element('#myiframe').load(function () {
			console.log(angular.element('#myiframe')[0].contentDocument.URL);

		});
		$scope.objectId = $stateParams.objectId;
		$scope.objectInfo = null;
		$scope.actionButton = null;
		$scope.returnButton = null;
		$scope.captionButton = "Loading...";
		$scope.objectType = null;
		$scope.message = "";
		$scope.alert = false;
		$scope.refreshIframe = false;
		$scope.$evalAsync($http.get(
			shAPIServerService.get().concat(
				"/v2/object/" + $scope.objectId + "/path")).then(
					function (response) {
						$scope.objectInfo = response.data;
						if ($scope.objectInfo.shPost == null) {
							if ($scope.objectInfo.currentFolder == null) {
								if ($scope.objectInfo.shSite != null) {
									$scope.captionButton = "Edit the Site";
									$scope.actionButton = "/content#!/site/"
										+ $scope.objectId + "/edit";
									$scope.returnButton = "/content#!/list/"
										+ $scope.objectId;
									$scope.objectType = "SITE";
								}
							} else {
								$scope.captionButton = "Edit the Folder";
								$scope.actionButton = "/content#!/folder/"
									+ $scope.objectId + "/edit";

								if ($scope.objectInfo.currentFolder.parentFolder != null) {
									$scope.returnButton = "/content#!/list/"
										+ $scope.objectInfo.currentFolder.parentFolder.id;
								}
								else {
									$scope.returnButton = "/content#!/list/"
										+ $scope.objectInfo.shSite.id;
								}
								$scope.objectType = "FOLDER";
							}
						} else {
							$scope.captionButton = "Edit the Post";
							$scope.actionButton = "/content#!/post/type/"
								+ $scope.objectInfo.shPost.shPostType.id
								+ "/post/" + $scope.objectId;
							$scope.returnButton = "/content#!/list/"
								+ $scope.objectInfo.shPost.shFolder.id;
							$scope.objectType = "POST";
						}
					}));
		$scope.previewURL = $sce
			.trustAsResourceUrl(shAPIServerService.get().concat(
				"/v2/object/" + $scope.objectId + "/preview"));
		$scope.isMobile = false;

		$scope.openNewWindow = function () {
			$window.open($scope.previewURL, "_blank");
		}

		$scope.actionLink = function () {
			$window.open($scope.actionButton, "_self");
		}
		$scope.returnLink = function () {
			$window.open($scope.returnButton, "_self");
		}

		$scope.objectClearCache = function () {
			$http.get(shAPIServerService.get().concat("/v2/object/" + $scope.objectId + "/clear-cache")).then(function (response) {
				var shObject = response.data;
				var clearMessage = null;
				if ($scope.objectType == "POST") {
					clearMessage = 'The Post Cache has been cleared.';
				}
				else if ($scope.objectType == "FOLDER") {
					clearMessage = 'The Folder Cache has been cleared.';
				}
				$scope.message = clearMessage;
				$scope.alert = true;
				$scope.resfeshIframe();
				$timeout(function () {
					$scope.alert = false;
					$scope.message = "";

				}, 2000);
			});
		}

		$scope.resfeshIframe = function () {
			$scope.previewURL = $scope.previewURL +"?a=1";
			//$scope.refreshIframe = true;
		/*	$timeout(function () {
				$scope.refreshIframe = false;
			}, 2000);
			*/
		};
		

	}]);