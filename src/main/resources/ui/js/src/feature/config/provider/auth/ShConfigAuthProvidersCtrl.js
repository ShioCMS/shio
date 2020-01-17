shioharaApp.controller('ShConfigAuthProvidersCtrl', [
	"$scope",
	"$state",
	"$rootScope",
	"$http",
	"$filter",
	"shAPIServerService",
	"shAuthProviderResource",
	function ($scope, $state, $rootScope, $http, $filter, shAPIServerService, shAuthProviderResource) {
		$rootScope.$state = $state;

		$scope.authProviders = shAuthProviderResource.query({}, function () {
			$scope.authProviders = $filter('orderBy')($scope.authProviders, 'position');
		});

		$scope.sortableAuthProviders = {
			disabled: false,
			stop: function (e, ui) {
				var sortObject = {};
				var i = 1;
				angular.forEach($scope.authProviders, function (authProvider, key) {
					sortObject[authProvider.id] = authProvider.position;
				});
				var parameter = JSON.stringify(sortObject);
				$http.put(shAPIServerService.get().concat("/v2/provider/auth/sort"), parameter).then(function (response) { });
			}
		};

		$scope.changeSearchTextBox = function (shSearchFilter) {
			if (shSearchFilter.length > 0) {
				$scope.sortableAuthProviders.disabled = true;
			}
			else {
				$scope.sortableAuthProviders.disabled = false;
			}
		}

	}]);
