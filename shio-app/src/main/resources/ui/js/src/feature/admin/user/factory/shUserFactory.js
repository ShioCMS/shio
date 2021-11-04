shioApp.factory('shUserFactory', [
	'$uibModal', 'shUserResource', 'Notification', '$state',
	function ($uibModal, shUserResource, Notification, $state) {
		const varToString = varObj => Object.keys(varObj)[0];
		return {
			delete: function (shUser) {
				var modalInstance = this.modalDelete(shUser);
				modalInstance.result.then(function (removeInstance) {
					var deletedMessage = 'The ' + shUser.username + ' was deleted.';

					shUserResource
						.delete({
							id: shUser.username
						}, function () {
							Notification.error(deletedMessage);
							$state.go('admin.user', {}, { reload: true });
						});
				}, function () {
					// Selected NO
				});
			},
			save: function (shUser, isNew) {
				if (!isNew) {
					var updateMessage = 'The ' + shUser.username + ' was updated.';
					shUser.$update(function () {
						Notification.warning(updateMessage);
						$state.go('admin.user');
					});
				} else {
					var saveMessage = 'The ' + shUser.username + ' was saved.';
					shUserResource.save(shUser, function (response) {
						Notification.warning(saveMessage);
						isNew = false;
						$state.go('admin.user');
					});
				}
			},
			addGroups: function (shUser) {
				var modalInstance = this.modalSelectGroup(shUser);
				modalInstance.result.then(function (shGroups) {
					if (shUser.shGroups != null) {
						angular.forEach(shGroups, function (shGroup, key) {
							console.log(shGroup.name);
							shUser.shGroups.push(shGroup);
						});						
					}
					else {
						shUser.shGroups = shGroups;
					}
				}, function () {
					// Selected NO
				});
			}, modalDelete: function (shUser) {
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
							return shUser.username;
						}
					}
				});
			},
			modalSelectGroup: function (shUser) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true
					, ariaLabelledBy: 'modal-title'
					, ariaDescribedBy: 'modal-body'
					, templateUrl: 'template/admin/group/group-select-dialog.html'
					, controller: 'ShModalSelectGroupListCtrl'
					, controllerAs: varToString({ $ctrl })
					, size: null
					, appendTo: undefined
					, resolve: {
						username: function () {
							return shUser.username;
						}
					}
				});
			}
		}
	}]);
