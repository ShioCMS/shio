shioharaApp.controller('ShStaticFileUploadSelectCtrl', [
	'$scope',
	'shAPIServerService',
	'$http',
	'$uibModalInstance',
	'shWidgetFileFactory',
	'Upload',
	'$timeout',
	'folderId',
	function ($scope, shAPIServerService, $http, $uibModalInstance, shWidgetFileFactory, Upload, $timeout, folderId) {
		var $ctrl = this;
		$scope.closeButtonLabel = "Cancel";
		$scope.shFiles = null;
		$scope.hasFiles = false;
		$scope.startUpload = false;
		$scope.uploadCompleted = false;
		$scope.$watch('shFiles', function () {
			if ($scope.shFiles != null) {
				$ctrl.listFiles($scope.shFiles, null);
			}
		});
		$ctrl.shPostSelected = null;
		$ctrl.ok = function () {
			$uibModalInstance.close($ctrl.shPostSelected);
		};

		$ctrl.cancel = function () {
			if ($scope.startUpload) {
				$uibModalInstance.close("finished");
			}
			else {
				$uibModalInstance.dismiss('cancel');
			}
		};
		$scope.shSite = null;
		$scope.shFolders = null;
		$scope.shPosts = null;
		$scope.breadcrumb = null;
		$scope.currentFolder = null;

		// BEGIN Functions
		$ctrl.listFiles = function (files, errFiles) {
			$scope.hasFiles = true;
			$scope.files = files;
		}
		$ctrl.uploadFiles = function (files, errFiles) {

			$scope.startUpload = true;
			$scope.closeButtonLabel = "Close";

			//$scope.files = files;
			angular.forEach($scope.files, function (file) {
				$scope.f = file;
				$scope.errFile = errFiles && errFiles[0];

				if (file) {
					file.error = null;
					file.upload = Upload.upload({
						url: shAPIServerService.get().concat('/v2/staticfile/upload'),
						data: {
							file: file,
							folderId: folderId,
							createPost: true
						}
					});
					file.upload.then(function (response) {
						$timeout(function () {
							//		$scope.shPosts.unshift(response.data);
							//		file.result = response.data;
						});
					}, function (response) {
						if (response.status > 0)
							file.error = response.data.title + ". " +  response.data.message;
					}, function (evt) {
						file.progress = Math.min(100, parseInt(100.0 *
							evt.loaded / evt.total));
					});
				}
			});
		}
		// END Functions

	}]);
