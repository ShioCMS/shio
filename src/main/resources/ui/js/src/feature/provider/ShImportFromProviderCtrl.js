shioharaApp.controller('ShImportFromProviderCtrl', [
	'$scope',
	'shAPIServerService',
	'$http',
	'$uibModalInstance',
	'objectType',
	function ($scope, shAPIServerService, $http, $uibModalInstance, objectType) {
		var $ctrl = this;
		$ctrl.loadData = false;
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

		$scope.isProviderList = false;
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

		$scope.rootList = function (providerId) {
			$scope.providerId = providerId;
			$scope.objectList("_root");
		}

		$scope.objectList = function (folderId) {
			if (!$ctrl.loadData) {
				$ctrl.loadData = true;
				delete $ctrl.providers;
				$scope.isProviderList = false;
				$scope.currentFolderId = folderId;
				$ctrl.enableInsertButton = false;
				$ctrl.shObjectSelected = null;
				var objectListURL = "/v2/provider/exchange/" + $scope.providerId + "/" + folderId + "/list/";

				$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(objectListURL))
					.then(function (response) {
						$ctrl.providerItem = response.data;
						$ctrl.loadData = false;
					}));
			}
		}

		$scope.providerList = function () {
			if (!$ctrl.loadData) {
				$ctrl.loadData = true;
				delete $ctrl.providerItem;
				$scope.isProviderList = true;
				$ctrl.enableInsertButton = false;
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
						"/v2/provider/exchange/"))
					.then(function (response) {
						$ctrl.providers = response.data;
						$ctrl.loadData = false;
					}));
			}
		}
		$scope.selectedObject = function (shObject) {
			$ctrl.shObjectSelected = shObject;
			$ctrl.enableInsertButton = true;
		}

		// END Functions

		$scope.providerList();
	}]);
