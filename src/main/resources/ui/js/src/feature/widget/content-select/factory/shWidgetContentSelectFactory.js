shioApp.factory('shWidgetContentSelectFactory', [ '$uibModal', 'shPostResource',
		'Notification', '$filter', "$state",
		function($uibModal, shPostResource, Notification, $filter, $state) {
			return {
				modalSelectFile : function(shFolder) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/widget/content-select/content-select-select.html',
						controller : 'ShWidgetContentSelectSelectCtrl',
						controllerAs : '$ctrl',
						size : null,
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
