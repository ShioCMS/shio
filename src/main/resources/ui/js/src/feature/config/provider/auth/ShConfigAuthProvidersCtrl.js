shioharaApp.controller('ShConfigAuthProvidersCtrl', [
	"$scope",
	"$state",
	"$rootScope",
	"$http",
	"$filter",
	"shAPIServerService",
	"shExchangeProviderResource",
	function ($scope, $state, $rootScope, $http, $filter, shAPIServerService, shExchangeProviderResource) {
		$rootScope.$state = $state;

		$scope.exchangeProviders = shExchangeProviderResource.query({}, function () {
			$scope.exchangeProviders = $filter('orderBy')($scope.exchangeProviders, 'position');
		});

		$scope.sortableExchangeProviders = {
			disabled: false,
			stop: function (e, ui) {
				var sortObject = {};
				var i = 1;
				angular.forEach($scope.exchangeProviders, function (exchangeProvider, key) {
					sortObject[exchangeProvider.id] = exchangeProvider.position;
				});
				var parameter = JSON.stringify(sortObject);
				$http.put(shAPIServerService.get().concat("/v2/provider/sort"), parameter).then(function (response) { });
			}
		};

		$scope.changeSearchTextBox = function (shSearchFilter) {
			if (shSearchFilter.length > 0) {
				$scope.sortableExchangeProviders.disabled = true;
			}
			else {
				$scope.sortableExchangeProviders.disabled = false;
			}
		}

	}]);
