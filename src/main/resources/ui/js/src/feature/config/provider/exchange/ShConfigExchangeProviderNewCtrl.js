shioharaApp.controller('ShConfigExchangeProviderNewCtrl', [
	"$scope",
	"$state",
	"$rootScope",
	"shExchangeProviderResource",
	function ($scope, $state, $rootScope, shExchangeProviderResource) {
		$rootScope.$state = $state;		
	}]);
