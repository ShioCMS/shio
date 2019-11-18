shioharaApp.controller('ShGroupEditCtrl', [
	"$scope",
	"$state",
	"$rootScope",
	"shGroupResource",
	"shGroupFactory",
	"$stateParams",
	"Notification",
	function ($scope, $state, $rootScope,
		shGroupResource, shGroupFactory, $stateParams, Notification) {
		$rootScope.$state = $state;
		$scope.groupId = $stateParams.groupId;

		$scope.group = shGroupResource.get({
			id: $scope.groupId
		});

		$scope.groupSave = function () {
			angular.forEach($scope.group.shUsers, function (shUser, key) {
				console.log("removendo atributo: " + shUser.username);
				delete shUser.shGroups;									
			});
			$scope.group.$update(function () {
				Notification.warning('The ' + $scope.group.name + ' Group was updated.');
			});
		}

		$scope.addUsers = function () {
			shGroupFactory.addUsers($scope.group);
		}

		$scope.removeUser = function (index) {
			$scope.group.shUsers.splice(index, 1);
		}
	}]);