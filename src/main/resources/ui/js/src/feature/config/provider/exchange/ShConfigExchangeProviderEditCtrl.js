shioharaApp.controller('ShConfigExchangeProviderEditCtrl', [
	"$scope",
	"$state",
	"$stateParams",
	"$rootScope",
	"shExchangeProviderResource",
	"shExchangeProviderVendorResource",
	function ($scope, $state, $stateParams, $rootScope, shExchangeProviderResource, shExchangeProviderVendorResource) {
		$rootScope.$state = $state;		
		$scope.exchangeProviderId = $stateParams.exchangeProviderId;
		
		$scope.vendors = shExchangeProviderVendorResource.query();
		$scope.exchangeProvider = shExchangeProviderResource.get({ id: $scope.exchangeProviderId});

	}]);
