shioharaApp.controller('ShECommercePaymentNewCtrl', [
		"$scope",
		"$http",
		"$rootScope",
		"$location",
		"shAPIServerService",
		"$state",
		"$window",
		"$translate",
		"vigLocale",
		"shEcommercePaymentTypeDefinitionResource",
		function($scope, $http, $rootScope, $location, shAPIServerService,
				$state, $window, $translate, vigLocale, shEcommercePaymentTypeDefinitionResource) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);
			$scope.paymentType = [];
			$scope.$evalAsync($http.get(shAPIServerService.get().concat("/api/v2/ecom/payment/type/model")).then(function (response) {            	 
                $scope.shPost = response.data;
                $scope.shPostType = response.data.shPostType;
            }));
			$scope.paymentTypeDefinitions = shEcommercePaymentTypeDefinitionResource.query();
		} ]);
