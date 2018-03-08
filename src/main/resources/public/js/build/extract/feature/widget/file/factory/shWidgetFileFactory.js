shioharaApp.factory('shWidgetFileFactory', [ '$uibModal', 'shPostResource',
		'Notification', '$filter', "$state",
		function($uibModal, shPostResource, Notification, $filter, $state) {
			return {
				modalSelectFile : function(channelId) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/widget/file/file-select.html',
						controller : 'ShWidgetFileSelectCtrl',
						controllerAs : '$ctrl',
						size : null,
						appendTo : undefined,
						resolve : {
							channelId : function() {
								return channelId;
							}
						}
					});
				}
			}
		} ]);