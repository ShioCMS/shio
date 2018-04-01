shioharaApp.controller('ShComponentExplorerCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance',
		'folderId',
		'objectType',
		'shWidgetFileFactory',
		function($scope, shAPIServerService, $http, $uibModalInstance,
				folderId, objectType, shWidgetFileFactory) {
			var $ctrl = this;
			$ctrl.objectTypeName = "Object";
			$ctrl.folderDoubleClick = false;
			if (angular.equals(objectType, "shFolder")) {
				$ctrl.objectTypeName = "Folder";
				$ctrl.folderDoubleClick = true;
			}
			$ctrl.enableInsertButton = false;
			$ctrl.shObjectSelected = null;
			$ctrl.ok = function() {
				$uibModalInstance.close($ctrl.shObjectSelected);
			};

			$ctrl.cancel = function() {
				$ctrl.shObjectSelected = null;
				$uibModalInstance.dismiss('cancel');
			};
			$scope.shSite = null;
			$scope.shFolders = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;

			// BEGIN Functions
			$scope.folderList = function(folderId) {
				$ctrl.enableInsertButton = false;
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/folder/" + folderId + "/list/" + objectType))
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
								"/v1/site/" + siteId + "/folder")).then(
						function(response) {
							$scope.shFolders = response.data.shFolders;
							$scope.shPosts = response.data.shPosts;
							$scope.shSite = response.data.shSite;
						}));
			}

			$scope.selectedPost = function(shPost) {
				$ctrl.shObjectSelected = shPost;
				$ctrl.enableInsertButton = true;
			}
			
			$scope.selectedFolder = function(shFolder) {
				$ctrl.shObjectSelected = shFolder;
				$ctrl.enableInsertButton = true;
			}
			// END Functions

			$scope.folderList(folderId);

		} ]);
