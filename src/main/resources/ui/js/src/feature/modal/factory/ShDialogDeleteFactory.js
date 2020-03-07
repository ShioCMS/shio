shioApp.factory('ShDialogDeleteFactory', [ '$uibModal', 'shPostResource',
		'Notification', '$filter', "$state",
		function($uibModal, shPostResource, Notification, $filter, $state) {
			return {				
				dialog : function(shObjects) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/dialog/delete/dialog-object-delete.html',
						controller : 'ShDialogDeleteObjectCtrl',
						controllerAs : '$ctrl',
						size : null,
						appendTo : undefined,
						resolve : {
							shObjects : function() {
								return shObjects;
							}
						}
					});
				}
			}
		} ]);
