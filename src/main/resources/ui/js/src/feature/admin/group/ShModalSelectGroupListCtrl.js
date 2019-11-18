shioharaApp.controller('ShModalSelectGroupListCtrl', [
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
			console.log("ok");
            for (var stateKey in $ctrl.shStateObjects) {
				console.log($ctrl.shStateObjects[stateKey]);
                if ($ctrl.shStateObjects[stateKey] === true) {
					console.log("Add");
					objects.push($ctrl.shObjects[stateKey]);
                }
			}
			console.log("Valida");
			angular.forEach(objects, function (shGroup, key) {
				console.log(shGroup.name)
			});
			console.log("Termina");
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
			console.log("checkSomeItemSelected");
			for (var stateKey in $ctrl.shStateObjects) {
				console.log($ctrl.shStateObjects[stateKey]);
            }
		}
		$ctrl.selectEverything = function () {
			if ($ctrl.checkAll) {
				for (var stateKey in $ctrl.shStateObjects) {
					$ctrl.shStateObjects[stateKey] = true;
				}
			}
			else {
				for (var stateKey in $ctrl.shStateObjects) {
					$ctrl.shStateObjects[stateKey] = false;
				}
			}
			$ctrl.checkSomeItemSelected();
		}
	}]);
