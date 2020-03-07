shioApp.factory('shGroupFactory', [
	'$uibModal', 'shGroupResource', 'Notification', '$state',
	function ($uibModal, shGroupResource, Notification, $state) {
		return {
			delete: function (shGroup) {
				var modalInstance = this.modalDelete(shGroup);
				modalInstance.result.then(function (removeInstance) {
					deletedMessage = 'The ' + shGroup.name + ' was deleted.';

					shGroupResource
						.delete({
							id: shGroup.id
						}, function () {
							Notification.error(deletedMessage);
							$state.go('admin.group', {}, { reload: true });
						});
				}, function () {
					// Selected NO
				});
			},
			save: function (shGroup) {
				if (shGroup.id > 0) {
					var updateMessage = 'The ' + shGroup.name + ' was saved.';
					shGroup.$update(function () {
						Notification.warning(updateMessage);
						$state.go('admin.group');
					});
				} else {
					var saveMessage = 'The ' + shGroup.name + ' was updated.';
					delete shGroup.id;
					shGroupResource.save(shGroup, function (response) {
						Notification.warning(saveMessage);
						$state.go('admin.group');
					});
				}
			}, addUsers: function (shGroup) {
				var modalInstance = this.modalSelectUser(shGroup);
				modalInstance.result.then(function (shUsers) {
					if (shGroup.shUsers != null) {
						angular.forEach(shUsers, function (shUser, key) {
							console.log(shUser.username);							
							shGroup.shUsers.push(shUser);
						});
					}
					else {
						shGroup.shUsers = shUsers;
					}
				}, function () {
					// Selected NO
				});
			},
			modalDelete: function (shGroup) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true
					, ariaLabelledBy: 'modal-title'
					, ariaDescribedBy: 'modal-body'
					, templateUrl: 'template/admin/user/user-delete.html'
					, controller: 'ShModalDeleteUserCtrl'
					, controllerAs: '$ctrl'
					, size: null
					, appendTo: undefined
					, resolve: {
						title: function () {
							return shGroup.name;
						}
					}
				});
			},
			modalSelectUser: function (shGroup) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true
					, ariaLabelledBy: 'modal-title'
					, ariaDescribedBy: 'modal-body'
					, templateUrl: 'template/admin/user/user-select-dialog.html'
					, controller: 'ShModalSelectUserListCtrl'
					, controllerAs: '$ctrl'
					, size: null
					, appendTo: undefined
					, resolve: {
						groupId: function () {
							return shGroup.id;
						}
					}
				});
			}
		}
	}]);
