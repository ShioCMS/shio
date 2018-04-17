shioharaApp.factory('shUserFactory', [
	'$uibModal','shUserResource', 'shAPIServerService', 'Notification','$state',
		function($uibModal,shUserResource, shAPIServerService, Notification, $state) {
			return {
				delete : function(shUser) {
					var modalInstance = this.modalDelete(shUser);
					modalInstance.result.then(function(removeInstance) {
						deletedMessage = 'The ' + shUser.username +' was deleted.';
						
						shUserResource
						.delete({
							id : shUser.id
						},function() {
							Notification.error(deletedMessage);
							$state.go('content',{}, {reload: true});
						});
					}, function() {
						// Selected NO
					});
				}, 			
				save: function(shUser) {
					if (shUser.id > 0 ) {
						var updateMessage = 'The ' + shUser.username +' was saved.';
						shUser.$update(function() {
							Notification.warning(updateMessage);
							$state.go('organization.user');						
						});
					} else {
						var saveMessage = 'The ' + shUser.username +' was updated.';
						delete shUser.id;
						shUserResource.save(shUser, function(response){
							Notification.warning(saveMessage);
							$state.go('organization.user');
						});
					}	
				},
				modalDelete: function (shUser) {
	                var $ctrl = this;
	                return $uibModal.open({
	                    animation: true
	                    , ariaLabelledBy: 'modal-title'
	                    , ariaDescribedBy: 'modal-body'
	                    , templateUrl: 'template/modal/shDeleteObject.html'
	                    , controller: 'ShModalDeleteObjectCtrl'
	                    , controllerAs: '$ctrl'
	                    , size: null
	                    , appendTo: undefined
	                    , resolve: {
	                        shObject: function () {
	                            return shUser;
	                        }
	                    }
	                });
	            }						
			}
		} ]);
