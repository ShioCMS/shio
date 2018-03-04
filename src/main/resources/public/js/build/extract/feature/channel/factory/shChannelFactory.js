shioharaApp.factory('shChannelFactory', [
	'$uibModal','shChannelResource', 'Notification','$filter',
		function($uibModal,shChannelResource, Notification, $filter) {
			return {
				
				delete : function(shChannel, shChannels) {
					var $ctrl = this;
					
					var modalInstance = $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/modal/shDeleteObject.html',
						controller : 'ShModalDeleteObjectCtrl',
						controllerAs : '$ctrl',
						size : null,
						appendTo : undefined,
						resolve : {
							instanceName : function() {
								return shChannel.name;
							}
						}
					});
					
					modalInstance.result.then(function(removeInstance) {
						deletedMessage = 'The '
							+ shChannel.name
							+ ' Channel was deleted.';
						
						shChannelResource
						.delete({
							id : shChannel.id
						},function() {
							// filter the array
						    var foundItem = $filter('filter')(shChannels, { id: shChannel.id }, true)[0];
						    // get the index
						    var index = shChannels.indexOf(foundItem );
						    // remove the item from array
						    shChannels.splice(index, 1);    		
							Notification.error(deletedMessage);
						});
					}, function() {
						// Selected NO
					});
				}						
			}
		} ]);