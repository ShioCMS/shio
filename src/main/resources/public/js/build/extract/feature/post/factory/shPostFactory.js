shioharaApp.factory('shPostFactory', [
	'$uibModal','shPostResource', 'Notification','$filter',
		function($uibModal,shPostResource, Notification, $filter) {
			return {
				delete : function(shPost, shPosts) {
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
								return shPost.title;
							}
						}
					});
			
					modalInstance.result.then(function(removeInstance) {
						deletedMessage = 'The '
						+ shPost.title
						+ ' Post was deleted.';
						
						shPostResource
						.delete({
							id : shPost.id
						},function() {
							// filter the array
						    var foundItem = $filter('filter')(shPosts, { id: shPost.id  }, true)[0];
						    // get the index
						    var index = shPosts.indexOf(foundItem );
						    // remove the item from array
						    shPosts.splice(index, 1);    		
							Notification.error(deletedMessage);
						});
					}, function() {
						// Selected NO
					});
				}
			}
		} ]);