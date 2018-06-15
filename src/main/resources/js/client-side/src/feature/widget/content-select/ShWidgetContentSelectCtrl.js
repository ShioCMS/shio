shioharaApp.controller('ShWidgetContentSelectCtrl', [
		'$scope',
		'Upload',
		'$timeout',
		'$uibModal',
		'ShDialogSelectObject',
		function($scope, Upload, $timeout, $uibModal, ShDialogSelectObject) {
			$scope.breadcrumb = [];
			$scope.init = function(shPostAttr) {
				if (shPostAttr.referenceObjects[0].objectType == "FOLDER") {
					$scope.folderBreadcrumb($scope.breadcrumb, shPostAttr.referenceObjects[0].parentFolder);
				} else if (shPostAttr.referenceObjects[0].objectType == "POST") {
					$scope.folderBreadcrumb($scope.breadcrumb, shPostAttr.referenceObjects[0].shFolder);
				}
			}
			$scope.folderBreadcrumb = function(breadcrumb, shFolder) {
				if (shFolder != null) {
					$scope.breadcrumbItem = {};
					$scope.breadcrumbItem.id = shFolder.id;
					$scope.breadcrumbItem.name = shFolder.name;
					$scope.breadcrumb.push($scope.breadcrumbItem);	
					if (shFolder.parentFolder != null) {
						$scope.folderBreadcrumb(breadcrumb, shFolder.parentFolder);
					} else if (shFolder.shSite != null) {
						$scope.breadcrumbItem = {};
						$scope.breadcrumbItem.id = shFolder.shSite.id;
						$scope.breadcrumbItem.name = shFolder.shSite.name + " (Site)";
						$scope.breadcrumb.push($scope.breadcrumbItem);
					}
				}			
			}
			$scope.$watch('shPostAttr.file', function() {
				if ($scope.shPostAttr.file != null) {
					$scope.uploadNewFile = false;
					$scope.shPostAttr.strValue = $scope.fileName;
				}
			});

			$scope.clearContent = function(shPostAttr) {
				shPostAttr.strValue = null;
				shPostAttr.file = null;
			}

			$scope.selectContent = function(shPost, shPostAttr) {
				var modalInstance = ShDialogSelectObject.dialog($scope.shFolder.id, "shObject");				
				modalInstance.result.then(function(shObjectSelected) {
					// Selected INSERT
					shPostAttr.strValue = shObjectSelected.id;
					shPostAttr.referenceObjects = [ shObjectSelected ];
				}, function() {
					// Selected CANCEL
				});
			}
			
			$scope.selectContentByObject = function(shPostAttr, shObject) {
				var modalInstance = ShDialogSelectObject.dialog(shObject.id, "shObject");				
				modalInstance.result.then(function(shObjectSelected) {
					// Selected INSERT
					shPostAttr.strValue = shObjectSelected.id;
					shPostAttr.referenceObjects = [ shObjectSelected ];
				}, function() {
					// Selected CANCEL
				});
			}
		} ]);
