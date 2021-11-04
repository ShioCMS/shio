shioApp.controller('ShECommercePaymentCtrl', [
		"$scope",
		"$http",
		"$rootScope",
		"$location",
		"shAPIServerService",
		"$state",
		"$window",
		"$translate",
		"vigLocale",
		"shEcommercePaymentTypeResource",
		function($scope, $http, $rootScope, $location, shAPIServerService,
				$state, $window, $translate, vigLocale, shEcommercePaymentTypeResource) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);	
			$scope.shEcomPaymentTypes = shEcommercePaymentTypeResource.query();
		} ]);
