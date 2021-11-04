shioApp.factory('ShDialogDeleteFactory', ['$uibModal',
	function ($uibModal) {
		const varToString = varObj => Object.keys(varObj)[0];
		return {
			dialog: function (shObjects) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true,
					ariaLabelledBy: 'modal-title',
					ariaDescribedBy: 'modal-body',
					templateUrl: 'template/dialog/delete/dialog-object-delete.html',
					controller: 'ShDialogDeleteObjectCtrl',
					controllerAs: varToString({ $ctrl }),
					size: null,
					appendTo: undefined,
					resolve: {
						shObjects: function () {
							return shObjects;
						}
					}
				});
			}
		}
	}]);
