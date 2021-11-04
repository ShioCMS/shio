shioApp.controller('ShComponentExplorerCtrl', [
	'$scope',
	'shAPIServerService',
	'$http',
	'$uibModalInstance',
	'objectId',
	'objectType',
	'shWidgetFileFactory',
	'Upload',
	'$timeout',
	function ($scope, shAPIServerService, $http, $uibModalInstance,
		objectId, objectType, shWidgetFileFactory, Upload, $timeout) {
		var $ctrl = this;
		$ctrl.objectTypeName = "Object";
		$ctrl.folderDoubleClick = false;

		if (angular.equals(objectType, "shFolder")) {
			$ctrl.objectTypeName = "Folder";
			$ctrl.folderDoubleClick = true;
		}
		else if (angular.equals(objectType, "shObject")) {
			$ctrl.objectTypeName = "Content or Folder";
			$ctrl.folderDoubleClick = true;
		}
		$ctrl.enableInsertButton = false;
		$ctrl.shObjectSelected = null;
		$ctrl.ok = function () {
			$uibModalInstance.close($ctrl.shObjectSelected);
		};

		$ctrl.cancel = function () {
			$ctrl.shObjectSelected = null;
			$uibModalInstance.dismiss('cancel');
		};
		$scope.shSite = null;
		$scope.shFolders = null;
		$scope.shPosts = null;
		$scope.breadcrumb = null;
		$scope.isSiteList = false;
		$scope.currentFolderId = null;
		$ctrl.orderAsc = true;
		$ctrl.orderBy = 'name';
		$ctrl.orderByPostSelected = "title";
        $ctrl.orderByFolderSelected = "name";
		$ctrl.shSearchFilter = "";

		// BEGIN Functions
		$ctrl.orderList = function () {
			if ($ctrl.orderAsc) {
				$ctrl.orderAsc = false;
			}
			else {
				$ctrl.orderAsc = true;
			}
			$ctrl.orderBySelection();
		}

		$ctrl.orderBySelection = function () {
            var orderPrefix = "";
            if (!$ctrl.orderAsc) {
                orderPrefix = "-";
            }
            
            if ($ctrl.orderBy === 'name') {
                $ctrl.orderByPostSelected = orderPrefix + "title";
                $ctrl.orderByFolderSelected = orderPrefix + "name";            
            }
            if ($ctrl.orderBy === 'date') {
                $ctrl.orderByPostSelected = orderPrefix + "date";
                $ctrl.orderByFolderSelected = orderPrefix + "date";
            }
		}
		
		$scope.showSearchFolderAction = function () {
			$scope.searchFolder = true;
			focus('search-folder-input');
		}

		$scope.hideSearchFolderAction = function () {
			$scope.searchFolder = false;
			$ctrl.shSearchFilter = "";
		}

		$scope.objectList = function (selectedObjectId) {
			$scope.currentFolderId = selectedObjectId;
			$scope.isSiteList = false;
			$ctrl.enableInsertButton = false;
			$ctrl.shObjectSelected = null;
			var objectListURL = "/v2/object/" + selectedObjectId + "/list/";

			if (!angular.equals(objectType, "shObject")) {
				objectListURL = objectListURL + objectType;
			}

			$scope.$evalAsync($http.get(
				shAPIServerService.get().concat(objectListURL))
				.then(function (response) {
					$scope.shSites = null;
					$scope.shFolders = response.data.shFolders;
					$scope.shPosts = response.data.shPosts;
					$scope.breadcrumb = response.data.breadcrumb;
					$scope.shSite = response.data.shSite;
				}));
		}

		$scope.siteList = function () {
			$scope.isSiteList = true;
			$ctrl.enableInsertButton = false;
			$ctrl.shPostSelected = null;
			$scope.$evalAsync($http.get(
				shAPIServerService.get().concat(
					"/v2/site/"))
				.then(function (response) {
					$scope.shSites = response.data;
					$scope.shFolders = null;
					$scope.shPosts = null;
					$scope.breadcrumb = null;
				}));
		}
		$scope.selectedObject = function (shObject) {
			$ctrl.shObjectSelected = shObject;
			$ctrl.enableInsertButton = true;
		}

		$scope.uploadFile = function (file, errFiles) {
			$scope.f = file;
			$scope.errFile = errFiles && errFiles[0];
			if (file) {
				file.upload = Upload.upload({
					url: shAPIServerService.get().concat('/v2/staticfile/upload'),
					data: {
						file: file,
						folderId: $scope.currentFolderId,
						createPost: true
					}
				});
				file.upload.then(function (response) {
					$timeout(function () {
						console.log("Adicionado o Arquivo");
						$scope.shPosts.unshift(response.data);
						file.result = response.data;
					});
				}, function (response) {
					if (response.status > 0)
						$scope.errorMsg = response.status + ': ' + response.data;
				}, function (evt) {
					file.progress = Math.min(100, parseInt(100.0 *
						evt.loaded / evt.total));
				});
			}
		}
		// END Functions

		$scope.objectList(objectId);

	}]);
