shioharaApp.controller('ShWidgetFileCtrl', [
		'$scope',
		'Upload',
		'$timeout',
		'$uibModal',
		'shWidgetFileFactory',
		function($scope, Upload, $timeout, $uibModal, shWidgetFileFactory) {
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

			$scope.selectFile = function(shPost, shPostAttr) {
				var modalInstance = shWidgetFileFactory
						.modalSelectFile($scope.channelId);
				modalInstance.result.then(function(shPostSelected) {
					// Selected INSERT
					$scope.uploadNewFile = false;
					$scope.fileName = shPostSelected.title;
					shPostAttr.strValue = shPostSelected.id;
					shPostAttr.referenceObjects = [ shPostSelected ];
				}, function() {
					// Selected CANCEL
				});
			}
		} ]);