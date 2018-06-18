shioharaApp.controller('ShWidgetContentSelectCtrl', [
		'$scope',
		'Upload',
		'$timeout',
		'$uibModal',
		'ShDialogSelectObject',
		function($scope, Upload, $timeout, $uibModal, ShDialogSelectObject) {
			$scope.breadcrumbCS = [];
			$scope.init = function(shPostAttr) {
				$scope.updateCSInfo(shPostAttr);
			}
			$scope.folderBreadcrumb = function(breadcrumb, shFolder) {
				if (shFolder != null) {
					var breadcrumbItem = {};
					breadcrumbItem.id = shFolder.id;
					breadcrumbItem.name = shFolder.name;
					breadcrumb.push(breadcrumbItem);	
					if (shFolder.parentFolder != null) {
						$scope.folderBreadcrumb(breadcrumb, shFolder.parentFolder);
					} else if (shFolder.shSite != null) {
						var breadcrumbItem = {};
						breadcrumbItem.id = shFolder.shSite.id;
						breadcrumbItem.name = shFolder.shSite.name + " (Site)";
						breadcrumb.push(breadcrumbItem);
					}
				}			
			}
			$scope.$watch('shPostAttr.file', function() {
				if ($scope.shPostAttr.file != null) {
					$scope.uploadNewFile = false;
					$scope.shPostAttr.strValue = $scope.fileName;
				}
			});

			$scope.updateCSInfo = function(shPostAttr) {	
				$scope.breadcrumbCS = [];
				if (shPostAttr.referenceObjects[0].objectType == "FOLDER") {
					$scope.folderBreadcrumb($scope.breadcrumbCS, shPostAttr.referenceObjects[0].parentFolder);
				} else if (shPostAttr.referenceObjects[0].objectType == "POST") {
					$scope.folderBreadcrumb($scope.breadcrumbCS, shPostAttr.referenceObjects[0].shFolder);
				}
				
			}
			
			$scope.clearContent = function(shPostAttr) {
				shPostAttr.strValue = null;
				shPostAttr.file = null;
				$scope.breadcrumbCS = [];
			}

			$scope.selectContent = function(shPost, shPostAttr) {
				var modalInstance = ShDialogSelectObject.dialog($scope.shFolder.id, "shObject");				
				modalInstance.result.then(function(shObjectSelected) {
					// Selected INSERT
					shPostAttr.strValue = shObjectSelected.id;
					shPostAttr.referenceObjects = [ shObjectSelected ];
					$scope.updateCSInfo(shPostAttr);
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
					$scope.updateCSInfo(shPostAttr);
				}, function() {
					// Selected CANCEL
				});
			}
		} ]);
