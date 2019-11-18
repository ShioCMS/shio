shioharaApp.factory('shFolderFactory', [
	'$uibModal', 'shFolderResource', 'Notification', '$filter',
	function ($uibModal, shFolderResource, Notification, $filter) {
		return {

			deleteFromList: function (shFolder, shFolders) {
				var modalInstance = this.modalDelete(shFolder);
				modalInstance.result.then(function (removeInstance) {
					deletedMessage = 'The '
						+ shFolder.name
						+ ' Folder was deleted.';

					shFolderResource
						.delete({
							id: shFolder.id
						}, function () {
							// filter the array
							var foundItem = $filter('filter')(shFolders, { id: shFolder.id }, true)[0];
							// get the index
							var index = shFolders.indexOf(foundItem);
							// remove the item from array
							shFolders.splice(index, 1);
							Notification.error(deletedMessage);
						});
				}, function () {
					// Selected NO
				});
			},
			openProperties: function (shFolder) {
				var modalInstance = this.modalProperties(shFolder);
				modalInstance.result.then(function (shGroups) {
					shFolder.shGroups = shGroups;
				}, function () {
					// Selected NO
				});
			},
			addGroups: function (shFolderGroups) {
				var modalInstance = this.modalSelectGroup(shFolderGroups);
				modalInstance.result.then(function (shGroups) {
					if (shFolderGroups != null) {
						angular.forEach(shGroups, function (shGroup, key) {
							shFolderGroups.push(shGroup);
						});
					}
					else {
						shFolderGroups = shGroups;
					}
				}, function () {
					// Selected NO
				});
			},
			addUsers: function (shFolderUsers) {
				var modalInstance = this.modalSelectUser(shFolderUsers);
				modalInstance.result.then(function (shUsers) {
					if (shFolderUsers != null) {
						angular.forEach(shUsers, function (shUser, key) {
							shFolderUsers.push(shUser.username);
						});
					}
					else {
						angular.forEach(shUsers, function (shUser, key) {
							shFolderUsers.push(shUser.username);
						});
					}
				}, function () {
					// Selected NO
				});
			},
			modalDelete: function (shFolder) {
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
							return shFolder;
						}
					}
				});
			},
			modalProperties: function (shFolder) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true
					, ariaLabelledBy: 'modal-title'
					, ariaDescribedBy: 'modal-body'
					, templateUrl: 'template/folder/folder-properties.html'
					, controller: 'ShFolderPropertiesCtrl'
					, controllerAs: '$ctrl'
					, size: null
					, appendTo: undefined
					, resolve: {
						shFolder: function () {
							return shFolder;
						}
					}
				});
			},
			modalSelectGroup: function (shFolderGroups) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true
					, ariaLabelledBy: 'modal-title'
					, ariaDescribedBy: 'modal-body'
					, templateUrl: 'template/admin/group/group-select-dialog.html'
					, controller: 'ShModalSelectGroupListCtrl'
					, controllerAs: '$ctrl'
					, size: null
					, appendTo: undefined
					, resolve: {
						username: function () {
							return shFolderGroups;
						}
					}
				});
			},
			modalSelectUser: function (shFolderUsers) {
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
							return shFolderUsers;
						}
					}
				});
			}
		}
	}]);
