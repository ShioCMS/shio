shioApp.factory('shWidgetFileFactory', ['$uibModal',
	function ($uibModal) {
		const varToString = varObj => Object.keys(varObj)[0];
		return {
			modalSelectFile: function (shFolder) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true,
					ariaLabelledBy: 'modal-title',
					ariaDescribedBy: 'modal-body',
					templateUrl: 'template/widget/file/file-select.html',
					controller: 'ShWidgetFileSelectCtrl',
					controllerAs: varToString({ $ctrl }),
					size: 'lg',
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
