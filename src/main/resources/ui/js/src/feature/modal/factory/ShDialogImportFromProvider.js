shioharaApp.factory('ShDialogImportFromProvider', ['$uibModal',
	function ($uibModal) {
		return {

			dialog: function (objectId, objectType) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true,
					ariaLabelledBy: 'modal-title',
					ariaDescribedBy: 'modal-body',
					templateUrl: 'template/dialog/provider/dialog-provider-select.html',
					controller: 'ShImportFromProviderCtrl',
					controllerAs: '$ctrl',
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
