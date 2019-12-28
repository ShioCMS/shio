shioharaApp.controller('ShModalDeleteGenericObjectCtrl', [
	"$uibModalInstance",
	"shObjectName",
	function ($uibModalInstance, shObjectName) {
		var $ctrl = this;
		$ctrl.removeInstance = false;
		$ctrl.objectName = shObjectName;

		$ctrl.ok = function () {
			$ctrl.removeInstance = true;
			$uibModalInstance.close($ctrl.removeInstance);
		};

		$ctrl.cancel = function () {
			$ctrl.removeInstance = false;
			$uibModalInstance.dismiss('cancel');
		};
	}]);
