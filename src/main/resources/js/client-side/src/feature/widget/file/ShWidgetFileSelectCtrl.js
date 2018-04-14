shioharaApp.controller('ShWidgetFileSelectCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance',
		'shFolder',
		'shWidgetFileFactory',
		function($scope, shAPIServerService, $http, $uibModalInstance,
				shFolder,shWidgetFileFactory) {
			var $ctrl = this;

			$ctrl.shPostSelected = null;
			$ctrl.ok = function() {
				$uibModalInstance.close($ctrl.shPostSelected);
			};

			$ctrl.cancel = function() {
				$ctrl.shPostSelected = null;
				$uibModalInstance.dismiss('cancel');
			};
			$scope.shSite = null;
			$scope.shFolders = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;

			// BEGIN Functions
			$scope.folderList = function(shFolder) {
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/object/" + shFolder.shGlobalId.id + "/list/PT-FILE"))
						.then(function(response) {
							$scope.shFolders = response.data.shFolders;
							$scope.shPosts = response.data.shPosts;
							$scope.breadcrumb = response.data.breadcrumb;
							$scope.shSite = response.data.shSite;
						}));
			}

			$scope.siteList = function(siteId) {
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/site/" + siteId + "/folder")).then(
						function(response) {
							$scope.shFolders = response.data.shFolders;
							$scope.shPosts = response.data.shPosts;
							$scope.shSite = response.data.shSite;
						}));
			}

			$scope.selectedPost = function(shPost) {
				$ctrl.shPostSelected = shPost;
			}
			// END Functions

			$scope.folderList(shFolder);

		} ]);
