shioharaApp.controller('ShConfigExchangeProvidersCtrl', [
	"$scope",
	"$state",
	"$rootScope",
	"shExchangeProviderResource",
	function ($scope, $state, $rootScope, shExchangeProviderResource) {
		$rootScope.$state = $state;
		
		$scope.exchangeProviders = shExchangeProviderResource.query();
		
	}]);
