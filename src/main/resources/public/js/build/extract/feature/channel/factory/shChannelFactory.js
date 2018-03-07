shioharaApp.factory('shChannelFactory', [
	'$uibModal','shChannelResource', 'Notification','$filter',
		function($uibModal,shChannelResource, Notification, $filter) {
			return {
				
				deleteFromList : function(shChannel, shChannels) {
					var modalInstance = this.modalDelete(shChannel);
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
				}, 
				modalDelete: function (shChannel) {
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
	                            return shChannel;
	                        }
	                    }
	                });
	            }						
			}
		} ]);