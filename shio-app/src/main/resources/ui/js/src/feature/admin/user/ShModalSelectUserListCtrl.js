shioApp.controller('ShModalSelectUserListCtrl', [
	"$uibModalInstance",
	"groupId",
	"shUserResource",
	function ($uibModalInstance, groupId, shUserResource) {
		var $ctrl = this;
		$ctrl.groupId = groupId;
		$ctrl.checkAll = false;
		$ctrl.shStateObjects = [];
		$ctrl.shObjects = [];
		$ctrl.shUsers = shUserResource.query({}, function () {
			angular.forEach($ctrl.shUsers, function (shUser, key) {
				$ctrl.shStateObjects[shUser.username] = false;
				$ctrl.shObjects[shUser.username] = shUser;
			});
		});

		$ctrl.itemSelected = false;
		$ctrl.ok = function () {			
			var objects = [];
            for (var stateKey in $ctrl.shStateObjects) {
				console.log($ctrl.shStateObjects[stateKey]);
                if ($ctrl.shStateObjects[stateKey] === true) {
					console.log("Add");
					objects.push($ctrl.shObjects[stateKey]);
                }
			}
			angular.forEach(objects, function (shUser, key) {
				console.log(shUser.name)
			});
			$uibModalInstance.close(objects);
		};

		$ctrl.cancel = function () {
			$ctrl.removeInstance = false;
			$uibModalInstance.dismiss('cancel');
		};

		$ctrl.checkSomeItemSelected = function () {
			$ctrl.itemSelected = false;
			for (var stateKey in $ctrl.shStateObjects) {
				if ($ctrl.shStateObjects[stateKey]) {
					$ctrl.itemSelected = true;
				}
			}
		}
		$ctrl.selectEverything = function () {
			if ($ctrl.checkAll) {
				for (var stateKey1 in $ctrl.shStateObjects) {
					$ctrl.shStateObjects[stateKey1] = true;
				}
			}
			else {
				for (var stateKey2 in $ctrl.shStateObjects) {
					$ctrl.shStateObjects[stateKey2] = false;
				}
			}
			$ctrl.checkSomeItemSelected();
		}
	}]);
