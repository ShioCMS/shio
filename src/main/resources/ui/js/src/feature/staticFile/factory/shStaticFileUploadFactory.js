shioApp.factory('shStaticFileUploadFactory', [ '$uibModal', 'shPostResource',
		'Notification', '$filter', "$state",
		function($uibModal, shPostResource, Notification, $filter, $state) {
			return {
				modalUploadFiles : function(folderId) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/staticfile/staticfile-upload.html',
						controller : 'ShStaticFileUploadSelectCtrl',
						controllerAs : '$ctrl',
						size : null,
						appendTo : undefined,
						resolve : {
							folderId : function() {
								return folderId;
							}
						}
					});
				}
			}
		} ]);
