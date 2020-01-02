shioharaApp.factory('shAuthProviderFactory', [
	'$uibModal', 'shAuthProviderResource', 'Notification', '$state',
	function ($uibModal, shAuthProviderResource, Notification, $state) {
		return {
			delete: function (shAuthProvider) {
				var modalInstance = this.modalDelete(shAuthProvider);
				modalInstance.result.then(function (removeInstance) {
					deletedMessage = 'The ' + shAuthProvider.name + ' Auth Provider was deleted.';

					shAuthProviderResource
						.delete({
							id: shAuthProvider.id
						}, function () {
							Notification.error(deletedMessage);
							$state.go('config.auth-providers', {}, { reload: true });
						});
				}, function () {
					// Selected NO
				});
			},
			modalDelete: function (shAuthProvider) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true
					, ariaLabelledBy: 'modal-title'
					, ariaDescribedBy: 'modal-body'
					, templateUrl: 'template/modal/shDeleteGenericObject.html'
					, controller: 'ShModalDeleteGenericObjectCtrl'
					, controllerAs: '$ctrl'
					, size: null
					, appendTo: undefined
					, resolve: {
						shObjectName: function () {
							return shAuthProvider.name;
						}
					}
				});
			}
		}
	}]);
