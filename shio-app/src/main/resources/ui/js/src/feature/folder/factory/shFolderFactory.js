shioApp.factory('shFolderFactory', [
	'$uibModal', 'shFolderResource', 'Notification', '$filter',
	function ($uibModal, shFolderResource, Notification, $filter) {
		const varToString = varObj => Object.keys(varObj)[0];
		return {
			deleteFromList: function (shFolder, shFolders) {
				var modalInstance = this.modalDelete(shFolder);
				modalInstance.result.then(function (removeInstance) {
					var deletedMessage = 'The '
						+ shFolder.name
						+ ' Folder was deleted.';

					shFolderResource
						.delete({
							id: shFolder.id
						}, function () {
							// filter the array
							var foundItem = $filter('filter')(shFolders, { id: shFolder.id }, true)[0];
							// get the index
							var index = shFolders.indexOf(foundItem);
							// remove the item from array
							shFolders.splice(index, 1);
							Notification.error(deletedMessage);
						});
				}, function () {
					// Selected NO
				});
			},			
			modalDelete: function (shFolder) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true
					, ariaLabelledBy: 'modal-title'
					, ariaDescribedBy: 'modal-body'
					, templateUrl: 'template/modal/shDeleteObject.html'
					, controller: 'ShModalDeleteObjectCtrl'
					, controllerAs: varToString({ $ctrl })
					, size: null
					, appendTo: undefined
					, resolve: {
						shObject: function () {
							return shFolder;
						}
					}
				});
			}
		}
	}]);
