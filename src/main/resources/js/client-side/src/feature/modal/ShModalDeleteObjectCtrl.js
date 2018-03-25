shioharaApp.controller('ShModalDeleteObjectCtrl', [
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
			shObjectType = $ctrl.shObject.shGlobalId.type

			if (angular.equals(shObjectType, 'POST')
					|| angular.equals(shObjectType, 'POSTTYPE')) {
				$ctrl.objectName = shObject.title;
			} else {
				$ctrl.objectName = shObject.name;
			}

			$http.get(
					shAPIServerService.get().concat(
							"/reference/to/" + $ctrl.shObject.shGlobalId.id))
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
									"/reference/to/" + shObject.shGlobalId.id + "/replace/" + shPostSelected.shGlobalId.id))
							.then(function(response) {
								$ctrl.objectRefers = response.data;
							});
					
				}, function() {
					// Selected CANCEL
				});
			}
		} ]);
