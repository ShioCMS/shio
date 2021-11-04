shioApp.controller('ShGroupNewCtrl', [
	"$scope",
	"$http",
	"$state",
	"$rootScope",
	"shGroupFactory",
	"shAPIServerService",
	function($scope, $http, $state, $rootScope,
			shGroupFactory, shAPIServerService) {
		$rootScope.$state = $state;
		$scope.group = {};
		$scope.$evalAsync($http.get(
				shAPIServerService.get().concat("/v2/group/model")).then(
				function(response) {
					$scope.group = response.data;
				}));
		$scope.groupSave = function() {
			shGroupFactory.save($scope.group);
		}
	} ]);