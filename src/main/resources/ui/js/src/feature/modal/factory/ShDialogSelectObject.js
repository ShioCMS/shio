shioApp.factory('ShDialogSelectObject', [ '$uibModal', 'shPostResource',
		'Notification', '$filter', "$state",
		function($uibModal, shPostResource, Notification, $filter, $state) {
			return {
				
				dialog : function(objectId, objectType) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/dialog/select/dialog-object-select.html',
						controller : 'ShComponentExplorerCtrl',
						controllerAs : '$ctrl',
						size : 'lg',
						appendTo : undefined,
						resolve : {
							objectId : function() {
								return objectId;
							},
							objectType : function() {
								return objectType;
							}
						}
					});
				}
			}
		} ]);
