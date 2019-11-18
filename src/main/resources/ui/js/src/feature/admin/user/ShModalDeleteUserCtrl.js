shioharaApp.controller('ShModalDeleteUserCtrl', [
	"$uibModalInstance",
	"title",
	function ($uibModalInstance, title) {
		var $ctrl = this;
		$ctrl.removeInstance = false;
		$ctrl.objectName = title;
		$ctrl.ok = function () {
			$ctrl.removeInstance = true;
			$uibModalInstance.close($ctrl.removeInstance);
		};

		$ctrl.cancel = function () {
			$ctrl.removeInstance = false;
			$uibModalInstance.dismiss('cancel');
		};
	}]);
