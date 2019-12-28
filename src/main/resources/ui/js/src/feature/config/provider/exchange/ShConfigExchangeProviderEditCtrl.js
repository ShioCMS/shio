shioharaApp.controller('ShConfigExchangeProviderEditCtrl', [
	"$scope",
	"$state",
	"$stateParams",
	"$rootScope",
	"shExchangeProviderResource",
	"shExchangeProviderVendorResource",
	"shExchangeProviderFactory",
	"Notification",
	function ($scope, $state, $stateParams, $rootScope, shExchangeProviderResource, shExchangeProviderVendorResource, shExchangeProviderFactory, Notification) {
		$rootScope.$state = $state;
		$scope.exchangeProviderId = $stateParams.exchangeProviderId;

		$scope.vendors = shExchangeProviderVendorResource.query();
		$scope.exchangeProvider = shExchangeProviderResource.get({ id: $scope.exchangeProviderId });
		$scope.exchangeProviderSave = function () {
			$scope.exchangeProvider.$update(function () {
				Notification.warning('The ' + $scope.exchangeProvider.name + ' Exchange Provider was updated.');
			});
		}
		$scope.exchangeProviderSaveAndClose = function () {
			$scope.exchangeProviderSave();
			$state.go('config.exchange-providers', {}, { reload: true });
		}

		$scope.removeExchangeProvider = function () {
			shExchangeProviderFactory.delete($scope.exchangeProvider);
		}

	}]);
