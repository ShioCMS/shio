shioharaApp.controller('ShComponentExplorerCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance',
		'objectId',
		'objectType',
		'shWidgetFileFactory',
		function($scope, shAPIServerService, $http, $uibModalInstance,
				objectId, objectType, shWidgetFileFactory) {
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
			$scope.objectList = function(objectList) {
				$ctrl.enableInsertButton = false;
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/object/" + objectId + "/list/" + objectType))
						.then(function(response) {
							$scope.shFolders = response.data.shFolders;
							$scope.shPosts = response.data.shPosts;
							$scope.breadcrumb = response.data.breadcrumb;
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

			$scope.objectList(objectId);

		} ]);
