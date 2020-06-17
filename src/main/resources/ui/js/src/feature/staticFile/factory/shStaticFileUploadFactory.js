shioApp.factory('shStaticFileUploadFactory', [ '$uibModal',
		function($uibModal) {
			const varToString = varObj => Object.keys(varObj)[0];
			return {
				modalUploadFiles : function(folderId) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/staticfile/staticfile-upload.html',
						controller : 'ShStaticFileUploadSelectCtrl',
						controllerAs : varToString({ $ctrl }),
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
