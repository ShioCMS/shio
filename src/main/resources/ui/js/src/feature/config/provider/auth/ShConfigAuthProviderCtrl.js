shioharaApp.controller('ShConfigAuthProviderCtrl', [
	"$scope",
	"$state",
	"$stateParams",
	"$rootScope",
	"$http",
	"shExchangeProviderResource",
	"shExchangeProviderVendorResource",
	"shExchangeProviderFactory",
	"shAPIServerService",
	"Notification",
	function ($scope, $state, $stateParams, $rootScope, $http, shExchangeProviderResource, shExchangeProviderVendorResource, shExchangeProviderFactory, shAPIServerService, Notification) {
		$rootScope.$state = $state;
		$scope.exchangeProviderId = $stateParams.exchangeProviderId;
		$scope.isNew = false;
		$scope.vendors = shExchangeProviderVendorResource.query();

		if ($scope.exchangeProviderId !== null && typeof $scope.exchangeProviderId !== 'undefined') {
			$scope.exchangeProvider = shExchangeProviderResource.get({ id: $scope.exchangeProviderId });
		}
		else {
			$scope.isNew = true;
			$scope
				.$evalAsync($http
					.get(shAPIServerService.get().concat("/v2/provider/model"))
					.then(
						function (response) {
							$scope.exchangeProvider = response.data;
							$scope.exchangeProvider.name = "Untitled Exchange Provider"
						}));
		}

		$scope.exchangeProviderSave = function () {
			if ($scope.isNew) {
				shExchangeProviderResource.save($scope.exchangeProvider, function (response) {
					$scope.exchangeProvider = response;
					$scope.isNew = false;
					Notification.warning($scope.exchangeProvider.name + '  Exchange Provider was saved.');
				});
			}
			else {
				$scope.exchangeProvider.$update(function () {
					Notification.warning('The ' + $scope.exchangeProvider.name + ' Exchange Provider was updated.');
				});
			}
		}
		$scope.exchangeProviderSaveAndClose = function () {
			$scope.exchangeProviderSave();
			$state.go('config.exchange-providers', {}, { reload: true });
		}

		$scope.removeExchangeProvider = function () {
			shExchangeProviderFactory.delete($scope.exchangeProvider);
		}

	}]);
