shioApp.factory('shObjectFactory', [
	'$uibModal', 'shPostResource', 'Notification', '$filter', "$state"
, function ($uibModal, shPostResource, Notification, $filter, $state) {
        return {
            openProperties: function (shObject) {
				var modalInstance = this.modalProperties(shObject);
				modalInstance.result.then(function (shGroups) {
					shObject.shGroups = shGroups;
				}, function () {
					// Selected NO
				});
            },
            addGroups: function (shObjectGroups) {
				var modalInstance = this.modalSelectGroup(shObjectGroups);
				modalInstance.result.then(function (shGroups) {
					if (shObjectGroups != null) {
						angular.forEach(shGroups, function (shGroup, key) {
							shObjectGroups.push(shGroup.name);
						});
					}
				}, function () {
					// Selected NO
				});
			},
			addUsers: function (shObjectUsers) {
				var modalInstance = this.modalSelectUser(shObjectUsers);
				modalInstance.result.then(function (shUsers) {
					if (shObjectUsers != null) {
						angular.forEach(shUsers, function (shUser, key) {
							shObjectUsers.push(shUser.username);
						});
					}
				}, function () {
					// Selected NO
				});
			},            
            modalProperties: function (shObject) {
				var $ctrl = this;
				return $uibModal.open({
					animation: true
					, ariaLabelledBy: 'modal-title'
					, ariaDescribedBy: 'modal-body'
					, templateUrl: 'template/object/object-properties.html'
					, controller: 'ShObjectPropertiesCtrl'
					, controllerAs: '$ctrl'
					, size: null
					, appendTo: undefined
					, resolve: {
						shObject: function () {
							return shObject;
						}
					}
				});
            },             
            modalSelectGroup: function (shObjectGroups) {
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
							return shObjectGroups;
						}
					}
				});
			},
			modalSelectUser: function (shObjectUsers) {
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
							return shObjectUsers;
						}
					}
				});
			}
        }
		}]);
