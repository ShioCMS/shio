shioApp.controller('ShWidgetTinyMCECtrl', [
	'$scope',
	'shWidgetFileFactory',
	'shAPIServerService',
	'shFolderResource',
	'ShDialogSelectObject',
	'$q',
	'$http',
	function ($scope, shWidgetFileFactory,
		shAPIServerService, shFolderResource, ShDialogSelectObject, $q, $http) {

		$scope.fileName = null;
		$scope.uploadNewFile = false;
		$scope.$watch('shPostAttr.file', function () {
			if ($scope.shPostAttr.file != null) {
				$scope.uploadNewFile = false;
				$scope.fileName = $scope.shPostAttr.file.name;
				$scope.shPostAttr.strValue = $scope.fileName;
			}
		});
		$scope.imageURL = null;
		$scope.breadcrumbFile = [];
		$scope.shPostFile = null;
		$scope.init = function (shPost, shPostAttr) {
			$scope.shPostFile = shPost;
			$scope.updateFileInfo(shPostAttr);
		}

		$scope.updateFileInfo = function (shPostAttr) {
			$scope.breadcrumbFile = [];
			if ((typeof shPostAttr.referenceObject != 'undefined')
				&& (shPostAttr.referenceObject != null)) {
				$scope.folderBreadcrumb($scope.breadcrumbFile,
					shPostAttr.referenceObject.shFolder);
				$scope.getImageURL(shPostAttr.referenceObject);
			} else {
				$scope.getImageURL($scope.shPostFile);
			}

		}
		$scope.folderBreadcrumb = function (breadcrumb, shFolder) {
			if (shFolder != null) {
				var breadcrumbItem = {};
				breadcrumbItem.id = shFolder.id;
				breadcrumbItem.name = shFolder.name;
				breadcrumb.push(breadcrumbItem);
				if (shFolder.parentFolder != null) {
					$scope.folderBreadcrumb(breadcrumb,
						shFolder.parentFolder);
				} else if (shFolder.shSite != null) {
					var breadcrumbItem = {};
					breadcrumbItem.id = shFolder.shSite.id;
					breadcrumbItem.name = shFolder.shSite.name + " (Site)";
					breadcrumb.push(breadcrumbItem);
				}
			}
		}

		$scope.newFile = function () {
			$scope.uploadNewFile = true;
		}
		$scope.clearFile = function (shPostAttr) {
			shPostAttr.strValue = null;
			shPostAttr.file = null;
			shPostAttr.referenceObject = null;
			$scope.breadcrumbFile = [];
			$scope.imageURL = null;
		}

		$scope.modalExplorer = function () {
			var deferred = $q.defer();
			var modalInstance = shWidgetFileFactory
				.modalSelectFile($scope.shFolder);
			modalInstance.result.then(function (shPostSelected) {
				$http.get(shAPIServerService.get().concat('/v2/object/' + shPostSelected.id + '/url')
				).then(function (response) {
					var object = {
						"url": response.data.url,
						"label": response.data.label
					}
					deferred.resolve(object);
				});
			}, function () {
				deferred.resolve(null);
				// Selected CANCEL
			});
			return deferred.promise;
		}
		$scope.selectContent = function () {
			var deferred = $q.defer();
			var modalInstance = ShDialogSelectObject
				.dialog($scope.shFolder.id, "shObject");
			modalInstance.result
				.then(
					function (shObjectSelected) {
						$http.get(shAPIServerService.get().concat('/v2/object/' + shObjectSelected.id + '/url')
						).then(function (response) {
							var object = {
								"url": response.data.url,
								"label": response.data.label
							}
							deferred.resolve(object);
						});
					}, function () {
						deferred.resolve(null);
						// Selected CANCEL
					});
			return deferred.promise;
		}
		$scope.selectFileByObject = function (shPostAttr, shObject) {
			var modalInstance = shWidgetFileFactory
				.modalSelectFile(shObject);
			modalInstance.result.then(function (shPostSelected) {
				// Selected INSERT
				$scope.uploadNewFile = false;
				$scope.fileName = shPostSelected.title;
				shPostAttr.strValue = shPostSelected.id;
				shPostAttr.referenceObject = shPostSelected;
				$scope.updateFileInfo(shPostAttr);
			}, function () {
				// Selected CANCEL
			});
		}

		$scope.getImageURL = function (shPost) {
			$scope.imageURL = null;
			if (shPost != null && shPost.title != null && shPost.title.match(/.(jpg|jpeg|png|gif)$/i)) {
				$scope.imageURL = shAPIServerService.get().concat(
					"/v2/object/" + shPost.id + "/preview");
			}
		}

	}]);
