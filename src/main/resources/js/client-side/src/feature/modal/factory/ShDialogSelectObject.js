shioharaApp.factory('ShDialogSelectObject', [ '$uibModal', 'shPostResource',
		'Notification', '$filter', "$state",
		function($uibModal, shPostResource, Notification, $filter, $state) {
			return {
				
				dialog : function(folderId, objectType) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/dialog/select/dialog-object-select.html',
						controller : 'ShComponentExplorerCtrl',
						controllerAs : '$ctrl',
						size : null,
						appendTo : undefined,
						resolve : {
							folderId : function() {
								return folderId;
							},
							objectType : function() {
								return objectType;
							}
						}
					});
				}
			}
		} ]);
