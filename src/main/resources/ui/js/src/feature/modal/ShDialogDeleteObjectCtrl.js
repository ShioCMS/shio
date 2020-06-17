shioApp.controller('ShDialogDeleteObjectCtrl', [
		"$uibModalInstance",
		"shObjects",
		function($uibModalInstance, shObjects) {
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
