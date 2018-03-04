shioharaApp.factory('shSiteFactory', [
	'$uibModal','shSiteResource', 'Notification','$state',
		function($uibModal,shChannelResource, Notification, $state) {
			return {
				
				delete : function(shSite) {
					var modalInstance = this.modalDelete(shSite);
					modalInstance.result.then(function(removeInstance) {
						deletedMessage = 'The ' + shSite.name +' Site was deleted.';
						
						shSiteResource
						.delete({
							id : shSite.id
						},function() {
							Notification.error(deletedMessage);
							$state.go('content',{}, {reload: true});
						});
					}, function() {
						// Selected NO
					});
				}, 			
				save: function(shSite) {
					if (shSite.id != null) {
						var updateMessage = 'The ' + shSite.name +' Site was saved.';
						shSite.$update(function() {
							Notification.warning(updateMessage);
							$state.go('content.children.site-children',{siteId: shSite.id});						
						});
					} else {
						var saveMessage = 'The ' + shSite.name +' Site was updated.';
						delete $scope.shSite.id;
						shSiteResource.save(shSite, function(response){
							Notification.warning(saveMessage);
							$state.go('content.children.site-children',{siteId: response.id});
						});
					}	
				},
				modalDelete: function (shSite) {
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
	                        instanceName: function () {
	                            return shSite.name;
	                        }
	                    }
	                });
	            }						
			}
		} ]);