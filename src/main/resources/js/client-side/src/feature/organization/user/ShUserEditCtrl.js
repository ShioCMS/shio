shioharaApp.controller('ShUserEditCtrl', [
	"$scope",
	"$http",
	"$window",
	"$stateParams",
	"$state",
	"$rootScope",
	"$translate",
	"vigLocale",
	"shUserResource",
	"shUserFactory",
	function ($scope, $http, $window, $stateParams, $state, $rootScope, $translate, vigLocale,shUserResource, shUserFactory) {
		$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
		$translate.use($scope.vigLanguage);
		$rootScope.$state = $state;
		$scope.userId = $stateParams.userId;
		
		$scope.user = shUserResource.get({id: $stateParams.userId});
			
		$scope.userSave = function () {
			shUserFactory.save($scope.user);			
		}
	}
]);