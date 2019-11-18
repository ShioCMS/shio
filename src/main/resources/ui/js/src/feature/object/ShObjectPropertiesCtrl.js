shioharaApp.controller('ShObjectPropertiesCtrl', [
	"$uibModalInstance",
	"shObject",
	"shObjectFactory",
	"Notification",
	"$http",
	"shAPIServerService",
	function ($uibModalInstance, shObject, shObjectFactory, Notification, $http, shAPIServerService) {
		var $ctrl = this;
		$ctrl.shObject = shObject;
		$http.get(shAPIServerService.get().concat("/v2/object/" + $ctrl.shObject.id + "/security"))
			.then(
				function (response) {
					$ctrl.shSecurity = response.data;
				});

		$ctrl.ok = function () {
			var parameter = JSON.stringify($ctrl.shSecurity);
			$http.put(shAPIServerService.get().concat("/v2/object/" + $ctrl.shObject.id + "/security"), parameter)
				.then(
					function (response) {
						var message = null;
						if ($ctrl.shObject.objectType === 'POST') {
							message = 'The '
								+ $ctrl.shObject.title
								+ ' Post Properties was updated.'
						}
						else if ($ctrl.shObject.objectType === 'FOLDER') {
							message = 'The '
								+ $ctrl.shObject.name
								+ ' Folder Properties was updated.'

						}
						Notification.warning(message);
					});
			$uibModalInstance.close($ctrl.shSecurity);
		};

		$ctrl.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};

		$ctrl.addGroups = function () {
			shObjectFactory.addGroups($ctrl.shSecurity.shGroups);
		}

		$ctrl.addUsers = function () {
			shObjectFactory.addUsers($ctrl.shSecurity.shUsers);
		}

		$ctrl.removeGroup = function (index) {
			$ctrl.shSecurity.shGroups.splice(index, 1);
		}

		$ctrl.removeUser = function (index) {
			$ctrl.shSecurity.shUsers.splice(index, 1);
		}
	}]);
