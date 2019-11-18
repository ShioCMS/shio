shioharaApp.controller('ShFolderPropertiesCtrl', [
	"$uibModalInstance",
	"shFolder",
	"shFolderFactory",
	"Notification",
	"$http",
	"shAPIServerService",
	function ($uibModalInstance, shFolder, shFolderFactory, Notification, $http, shAPIServerService) {
		var $ctrl = this;
		$ctrl.shFolder = shFolder;
		$http.get(shAPIServerService.get().concat("/v2/folder/" + $ctrl.shFolder.id + "/security"))
			.then(
				function (response) {
					$ctrl.shSecurity = response.data;
				});

		$ctrl.ok = function () {
			var parameter = JSON.stringify($ctrl.shSecurity);
			$http.put(shAPIServerService.get().concat("/v2/folder/" + $ctrl.shFolder.id + "/security"), parameter)
				.then(
					function (response) {
						Notification.warning('The '
							+ $ctrl.shFolder.name
							+ ' Folder Properties was updated.');
					});
			$uibModalInstance.close($ctrl.shSecurity);
		};

		$ctrl.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};

		$ctrl.addGroups = function () {
			shFolderFactory.addGroups($ctrl.shSecurity.shGroups);
		}

		$ctrl.addUsers = function () {
			shFolderFactory.addUsers($ctrl.shSecurity.shUsers);
		}

		$ctrl.removeGroup = function (index) {
			$ctrl.shSecurity.shGroups.splice(index, 1);
		}

		$ctrl.removeUser = function (index) {
			$ctrl.shSecurity.shUsers.splice(index, 1);
		}
	}]);
