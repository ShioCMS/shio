shioApp.factory('shWidgetFileFactory', [ '$uibModal', 'shPostResource',
		'Notification', '$filter', "$state",
		function($uibModal, shPostResource, Notification, $filter, $state) {
			return {
				modalSelectFile : function(shFolder) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/widget/file/file-select.html',
						controller : 'ShWidgetFileSelectCtrl',
						controllerAs : '$ctrl',
						size : 'lg',
						appendTo : undefined,
						resolve : {
							shFolder : function() {
								return shFolder;
							}
						}
					});
				}
			}
		} ]);
