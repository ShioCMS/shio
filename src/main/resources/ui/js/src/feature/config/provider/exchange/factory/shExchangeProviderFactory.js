shioharaApp.factory('shExchangeProviderFactory', [
	'$uibModal', 'shExchangeProviderResource', 'Notification', '$state',
	function ($uibModal, shExchangeProviderResource, Notification, $state) {
		return {
			delete: function (shExchangeProvider) {
				var modalInstance = this.modalDelete(shExchangeProvider);
				modalInstance.result.then(function (removeInstance) {
					deletedMessage = 'The ' + shExchangeProvider.name + ' Exchange Provider was deleted.';

					shExchangeProviderResource
						.delete({
							id: shExchangeProvider.id
						}, function () {
							Notification.error(deletedMessage);
							$state.go('config.exchange-providers', {}, { reload: true });
						});
				}, function () {
					// Selected NO
				});
			},
			modalDelete: function (shExchangeProvider) {
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
							return shExchangeProvider.name;
						}
					}
				});
			}
		}
	}]);
