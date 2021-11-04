shioApp.controller('ShModalSelectGroupListCtrl', [
	"$uibModalInstance",
	"username",
	"shGroupResource",
	function ($uibModalInstance, username, shGroupResource) {
		var $ctrl = this;
		$ctrl.username = username;
		$ctrl.checkAll = false;
		$ctrl.shStateObjects = [];
		$ctrl.shObjects = [];
		$ctrl.shGroups = shGroupResource.query({}, function () {
			angular.forEach($ctrl.shGroups, function (shGroup, key) {
				$ctrl.shStateObjects[shGroup.id] = false;
				$ctrl.shObjects[shGroup.id] = shGroup;
			});
		});

		$ctrl.itemSelected = false;
		$ctrl.ok = function () {			
			var objects = [];
            for (var stateKey in $ctrl.shStateObjects) {
				console.log($ctrl.shStateObjects[stateKey]);
                if ($ctrl.shStateObjects[stateKey] === true) {
					objects.push($ctrl.shObjects[stateKey]);
                }
			}
			angular.forEach(objects, function (shGroup, key) {
				console.log(shGroup.name)
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
