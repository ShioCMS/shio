shioApp.factory('ShDialogImportFromProvider', ['$uibModal',
	function ($uibModal) {
		const varToString = varObj => Object.keys(varObj)[0];
		return {
			dialog: function (objectId, objectType) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true,
					ariaLabelledBy: 'modal-title',
					ariaDescribedBy: 'modal-body',
					templateUrl: 'template/dialog/provider/dialog-provider-select.html',
					controller: 'ShImportFromProviderCtrl',
					controllerAs: varToString({ $ctrl }),
					size: 'lg',
					appendTo: undefined,
					resolve: {
						objectId: function () {
							return objectId;
						},
						objectType: function () {
							return objectType;
						}
					}
				});
			}
		}
	}]);
