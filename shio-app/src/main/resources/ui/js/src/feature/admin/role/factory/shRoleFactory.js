shioApp.factory('shRoleFactory', [
	'$uibModal','shRoleResource', 'Notification','$state',
		function($uibModal,shRoleResource, Notification, $state) {
			const varToString = varObj => Object.keys(varObj)[0];
			return {
				delete : function(shRole) {
					var modalInstance = this.modalDelete(shRole);
					modalInstance.result.then(function(removeInstance) {
						var deletedMessage = 'The ' + shRole.name +' was deleted.';
						
						shRoleResource
						.delete({
							id : shRole.id
						},function() {
							Notification.error(deletedMessage);
							$state.go('admin.role',{}, {reload: true});
						});
					}, function() {
						// Selected NO
					});
				}, 			
				save: function(shRole) {
					if (shRole.id > 0 ) {
						var updateMessage = 'The ' + shRole.name +' was saved.';
						shRole.$update(function() {
							Notification.warning(updateMessage);
							$state.go('admin.role');						
						});
					} else {
						var saveMessage = 'The ' + shRole.name +' was updated.';
						delete shRole.id;
						shRoleResource.save(shRole, function(response){
							Notification.warning(saveMessage);
							$state.go('admin.role');
						});
					}	
				},
				modalDelete: function (shRole) {
					var $ctrl = this;
					return $uibModal.open({
						animation: true
						, ariaLabelledBy: 'modal-title'
						, ariaDescribedBy: 'modal-body'
						, templateUrl: 'template/admin/user/user-delete.html'
						, controller: 'ShModalDeleteUserCtrl'
						, controllerAs: varToString({ $ctrl })
						, size: null
						, appendTo: undefined
						, resolve: {
							title: function () {
								return shRole.name;
							}
						}
					});
				}					
			}
		} ]);
