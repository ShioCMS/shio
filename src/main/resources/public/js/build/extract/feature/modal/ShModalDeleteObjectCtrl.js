shioharaApp.controller('ShModalDeleteObjectCtrl', [
		"$uibModalInstance",
		"$http",
		"shObject",
		"shAPIServerService",
		function($uibModalInstance, $http, shObject, shAPIServerService) {
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
		} ]);