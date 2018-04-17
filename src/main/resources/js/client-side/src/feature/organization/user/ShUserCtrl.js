shioharaApp.controller('ShUserCtrl', [
	"$scope",
	"$http",
	"$window",
	"$state",
	"$rootScope",
	"$translate",
	"shUserResource",
	"shUserFactory",
	function ($scope, $http, $window, $state, $rootScope, $translate, shUserResource, shUserFactory) {
		$rootScope.$state = $state;
		$scope.users = null;
		$scope.users = shUserResource.query();

		$scope.userDelete = function (userId) {
			shUserFactory.delete($scope.shSite);
		}
	}]);
