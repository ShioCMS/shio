shioharaApp.controller('ShModalDeleteObjectCtrl', [ "$uibModalInstance",
		"instanceName", function($uibModalInstance, shPost) {
			var $ctrl = this;
			$ctrl.removeInstance = false;
			$ctrl.shPost = shPost;
			$ctrl.ok = function() {
				$ctrl.removeInstance = true;
				$uibModalInstance.close($ctrl.removeInstance);
			};

			$ctrl.cancel = function() {
				$ctrl.removeInstance = false;
				$uibModalInstance.dismiss('cancel');
			};
		} ]);