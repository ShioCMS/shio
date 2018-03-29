shioharaApp.controller('ShDialogDeleteObjectCtrl', [
		"$uibModalInstance",
		"$http",
		"shObjects",
		"shAPIServerService",
		"shWidgetFileFactory",
		function($uibModalInstance, $http, shObjects, shAPIServerService,
				shWidgetFileFactory) {
			var $ctrl = this;
			$ctrl.removeInstance = false;
			$ctrl.shObjects = shObjects;
		
			$ctrl.ok = function() {
				$uibModalInstance.close();
			};

			$ctrl.cancel = function() {
				$ctrl.removeInstance = false;
				$uibModalInstance.dismiss('cancel');
			};
		} ]);
