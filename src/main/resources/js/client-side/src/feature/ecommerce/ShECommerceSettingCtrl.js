shioharaApp.controller('ShECommerceSettingCtrl', [
		"$scope",
		"$http",
		"$rootScope",
		"$location",
		"shAPIServerService",
		"$state",
		"$window",
		"$translate",
		"vigLocale",
		function($scope, $http, $rootScope, $location, shAPIServerService,
				$state, $window, $translate, vigLocale) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);	
		} ]);
