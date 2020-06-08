shioApp.controller('ShModalDeleteObjectCtrl', [
		"$uibModalInstance",
		"$http",
		"shObject",
		"shAPIServerService",
		"shWidgetFileFactory",
		function($uibModalInstance, $http, shObject, shAPIServerService,
				shWidgetFileFactory) {
			var $ctrl = this;
			$ctrl.removeInstance = false;
			$ctrl.shObject = shObject;
			var shObjectType = $ctrl.shObject.objectType

			if (angular.equals(shObjectType, 'POST')
					|| angular.equals(shObjectType, 'POST_TYPE')) {
				$ctrl.objectName = shObject.title;
			} else {
				$ctrl.objectName = shObject.name;
			}

			$http.get(
					shAPIServerService.get().concat(
							"/v2/reference/to/" + $ctrl.shObject.id))
					.then(function(response) {
						$ctrl.objectRefers = response.data;
					});

			$ctrl.ok = function() {
				$ctrl.removeInstance = true;
				$uibModalInstance.close($ctrl.removeInstance);
			};

			$ctrl.cancel = function() {
				$ctrl.removeInstance = false;
				$uibModalInstance.dismiss('cancel');
			};

			$ctrl.selectFile = function() {
				var modalInstance = shWidgetFileFactory.modalSelectFile($ctrl.shObject.shFolder.id);
				modalInstance.result.then(function(shPostSelected) {
					// Selected Replace and Remove it
					$http.post(
							shAPIServerService.get().concat(
									"/v2/reference/to/" + shObject.id + "/replace/" + shPostSelected.id))
							.then(function(response) {
								$ctrl.objectRefers = response.data;
							});
					
				}, function() {
					// Selected CANCEL
				});
			}
		} ]);
