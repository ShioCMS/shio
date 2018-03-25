shioharaApp.controller('ShSiteListCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"shSiteResource",
		function($scope, $http, $window, $state, $rootScope, shSiteResource) {
			$rootScope.$state = $state;
			$scope.shSites = shSiteResource.query();
		} ]);
