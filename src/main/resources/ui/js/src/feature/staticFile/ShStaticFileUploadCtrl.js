shioharaApp.controller('ShStaticFileUploadCtrl', [
		"$uibModalInstance",
		"$http",
		"shObject",
		"shAPIServerService",
		"shStaticFileUploadFactory",
		function($uibModalInstance, $http, shObject, shAPIServerService,
			shStaticFileUploadFactory) {
			var $ctrl = this;
			$ctrl.removeInstance = false;
			$ctrl.shObject = shObject;
			shObjectType = $ctrl.shObject.objectType

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

			$ctrl.uploadFiles = function() {
				var modalInstance = shStaticFileUploadFactory.modalUploadFiles($ctrl.shObject.shFolder.id);
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
