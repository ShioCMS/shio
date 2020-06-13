shioApp.controller('ShWidgetContentSelectSelectCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance',
		'shFolder',
		'shWidgetContentSelectFactory',
		function($scope, shAPIServerService, $http, $uibModalInstance,
				shFolder,shWidgetContentSelectFactory) {
			var $ctrl = this;

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
			$scope.folderList = function(shFolderItem) {
				$ctrl.shObjectSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/object/" + shFolderItem.id + "/list"))
						.then(function(response) {
							$scope.shFolders = response.data.shFolders;
							$scope.shPosts = response.data.shPosts;
							$scope.breadcrumb = response.data.breadcrumb;
							$scope.shSite = response.data.shSite;
						}));
			}

			$scope.siteList = function(siteId) {
				$ctrl.shObjectSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/site/" + siteId + "/folder")).then(
						function(response) {
							$scope.shFolders = response.data.shFolders;
							$scope.shPosts = response.data.shPosts;
							$scope.shSite = response.data.shSite;
						}));
			}

			$scope.selectedObject = function(shObject) {
				$ctrl.shObjectSelected = shObject;
			}
			
			// END Functions

			$scope.folderList(shFolder);

		} ]);
