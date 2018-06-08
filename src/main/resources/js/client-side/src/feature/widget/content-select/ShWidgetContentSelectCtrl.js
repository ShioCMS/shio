shioharaApp.controller('ShWidgetContentSelectCtrl', [
		'$scope',
		'Upload',
		'$timeout',
		'$uibModal',
		'ShDialogSelectObject',
		function($scope, Upload, $timeout, $uibModal, ShDialogSelectObject) {
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
		} ]);
