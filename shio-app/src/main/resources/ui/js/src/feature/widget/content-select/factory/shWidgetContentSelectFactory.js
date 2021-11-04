shioApp.factory('shWidgetContentSelectFactory', ['$uibModal',
	function ($uibModal) {
		const varToString = varObj => Object.keys(varObj)[0];
		return {
			modalSelectFile: function (shFolder) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true,
					ariaLabelledBy: 'modal-title',
					ariaDescribedBy: 'modal-body',
					templateUrl: 'template/widget/content-select/content-select-select.html',
					controller: 'ShWidgetContentSelectSelectCtrl',
					controllerAs: varToString({ $ctrl }),
					size: null,
					appendTo: undefined,
					resolve: {
						shFolder: function () {
							return shFolder;
						}
					}
				});
			}
		}
	}]);
