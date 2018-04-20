shioharaApp.controller('ShWidgetContentSelectCtrl', [
		'$scope',
		'Upload',
		'$timeout',
		'$uibModal',
		'shWidgetContentSelectFactory',
		function($scope, Upload, $timeout, $uibModal, shWidgetContentSelectFactory) {
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
				var modalInstance = shWidgetContentSelectFactory
						.modalSelectFile($scope.shFolder);
				modalInstance.result.then(function(shPostSelected) {
					// Selected INSERT
					shPostAttr.strValue = shPostSelected.id;
					shPostAttr.referenceObjects = [ shPostSelected ];
				}, function() {
					// Selected CANCEL
				});
			}
		} ]);
