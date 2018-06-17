shioharaApp.controller('ShWidgetFileCtrl', [
		'$scope',
		'Upload',
		'$timeout',
		'$uibModal',
		'shWidgetFileFactory',
		'shAPIServerService',
		function($scope, Upload, $timeout, $uibModal, shWidgetFileFactory, shAPIServerService) {
			$scope.fileName = null;
			$scope.uploadNewFile = false;
			$scope.$watch('shPostAttr.file', function() {
				if ($scope.shPostAttr.file != null) {
					$scope.uploadNewFile = false;
					$scope.fileName = $scope.shPostAttr.file.name;
					$scope.shPostAttr.strValue = $scope.fileName;
				}
			});
			$scope.imageURL = null;
			$scope.breadcrumbFile = [];
			$scope.init = function(shPost, shPostAttr) {
				if (typeof shPostAttr.referenceObjects[0] != 'undefined' ) {
					$scope.folderBreadcrumb($scope.breadcrumbFile,
						shPostAttr.referenceObjects[0].shFolder);
					$scope.getImageURL(shPostAttr.referenceObjects[0]);
					if (shPostAttr.referenceObjects[0].title.match(/.(jpg|jpeg|png|gif)$/i)) {
						$scope.imageURL = shAPIServerService.get().concat("/v2/object/" + shPostAttr.referenceObjects[0].id + "/preview");
					}
				}
				else {
					$scope.getImageURL(shPost);
				}
				
			}

			$scope.folderBreadcrumb = function(breadcrumb, shFolder) {
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

			$scope.newFile = function() {
				$scope.uploadNewFile = true;
			}
			$scope.clearFile = function(shPostAttr) {
				shPostAttr.strValue = null;
				shPostAttr.file = null;
			}

			$scope.selectFile = function(shPost, shPostAttr) {
				var modalInstance = shWidgetFileFactory
						.modalSelectFile($scope.shFolder);
				modalInstance.result.then(function(shPostSelected) {
					// Selected INSERT
					$scope.uploadNewFile = false;
					$scope.fileName = shPostSelected.title;
					shPostAttr.strValue = shPostSelected.id;
					shPostAttr.referenceObjects = [ shPostSelected ];
					$scope.getImageURL(shPostAttr.referenceObjects[0]);
				}, function() {
					// Selected CANCEL
				});
			}

			$scope.selectFileByObject = function(shPostAttr, shObject) {
				var modalInstance = shWidgetFileFactory
						.modalSelectFile(shObject);
				modalInstance.result.then(function(shPostSelected) {
					// Selected INSERT
					$scope.uploadNewFile = false;
					$scope.fileName = shPostSelected.title;
					shPostAttr.strValue = shPostSelected.id;
					shPostAttr.referenceObjects = [ shPostSelected ];
					$scope.getImageURL(shPostAttr.referenceObjects[0]);
				}, function() {
					// Selected CANCEL
				});
			}
			
			$scope.getImageURL = function(shPost) {
				if (shPost.title.match(/.(jpg|jpeg|png|gif)$/i)) {
					$scope.imageURL = shAPIServerService.get().concat("/v2/object/" + shPost.id + "/preview");
				}
			}

		} ]);
