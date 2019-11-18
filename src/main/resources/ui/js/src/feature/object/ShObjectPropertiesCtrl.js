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

		$ctrl.addConsoleGroups = function () {
			shObjectFactory.addGroups($ctrl.shSecurity.console.shGroups);
		}

		$ctrl.addConsoleUsers = function () {
			shObjectFactory.addUsers($ctrl.shSecurity.console.shUsers);
		}

		$ctrl.removeConsoleGroup = function (index) {
			$ctrl.shSecurity.shGroups.splice(index, 1);
		}

		$ctrl.removeConsoleUser = function (index) {
			$ctrl.shSecurity.shUsers.splice(index, 1);
		}

		$ctrl.addPageGroups = function () {
			shObjectFactory.addGroups($ctrl.shSecurity.page.shGroups);
		}

		$ctrl.removePageGroup = function (index) {
			$ctrl.shSecurity.page.shGroups.splice(index, 1);
		}

	}]);
