shioharaApp.controller('ShWidgetRelatorCtrl', [
		'$scope',
		'Upload',
		'$timeout',
		'$uibModal',
		'shWidgetRelatorFactory',
		function($scope, Upload, $timeout, $uibModal, shWidgetRelatorFactory) {
			$scope.fileName = null;
			$scope.uploadNewFile = false;
			$scope.$watch('shPostAttr.file', function() {
				if ($scope.shPostAttr.file != null) {
					$scope.uploadNewFile = false;
					$scope.fileName = $scope.shPostAttr.file.name;
					$scope.shPostAttr.strValue = $scope.fileName;
				}
			});

			$scope.newFile = function() {
				$scope.uploadNewFile = true;
			}
			$scope.clearFile = function(shPostAttr) {
				shPostAttr.strValue = null;
				shPostAttr.file = null;
			}

			$scope.selectRelatorItem = function(shPost, shPostAttr) {
				var modalInstance = shWidgetRelatorFactory
						.modalSelectRelatorItem(shPostAttr);
				modalInstance.result.then(function(shPostSelected) {
					// Selected SAVE			
				}, function() {
					// Selected CANCEL
				});
			}
		} ]);
