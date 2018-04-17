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
			$scope.isSiteList = false;

			// BEGIN Functions
			$scope.objectList = function(selectedObjectId) {
				$scope.isSiteList = false;
				$ctrl.enableInsertButton = false;
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/object/" + selectedObjectId + "/list/" + objectType))
						.then(function(response) {
							$scope.shSites = null;	
							$scope.shFolders = response.data.shFolders;
							$scope.shPosts = response.data.shPosts;
							$scope.breadcrumb = response.data.breadcrumb;
							$scope.shSite = response.data.shSite;							
						}));
			}

			$scope.siteList = function() {
				$scope.isSiteList = true;
				$ctrl.enableInsertButton = false;
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/site/"))
						.then(function(response) {
							$scope.shSites = response.data;	
							$scope.shFolders = null;
							$scope.shPosts = null;
							$scope.breadcrumb = null;
						}));
			}
			$scope.selectedObject = function(shObject) {
				$ctrl.shObjectSelected = shObject;
				$ctrl.enableInsertButton = true;
			}
			
			// END Functions

			$scope.objectList(objectId);

		} ]);
