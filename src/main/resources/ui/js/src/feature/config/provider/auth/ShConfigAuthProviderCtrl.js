shioApp.controller('ShConfigAuthProviderCtrl', [
	"$scope",
	"$state",
	"$stateParams",
	"$rootScope",
	"$http",
	"shAuthProviderResource",
	"shAuthProviderVendorResource",
	"shAuthProviderFactory",
	"shAPIServerService",
	"Notification",
	function ($scope, $state, $stateParams, $rootScope, $http, shAuthProviderResource, shAuthProviderVendorResource, shAuthProviderFactory, shAPIServerService, Notification) {
		$rootScope.$state = $state;
		$scope.authProviderId = $stateParams.authProviderId;
		$scope.isNew = false;
		$scope.vendors = shAuthProviderVendorResource.query();

		if ($scope.authProviderId !== null && typeof $scope.authProviderId !== 'undefined') {
			$scope.authProvider = shAuthProviderResource.get({ id: $scope.authProviderId });
		}
		else {
			$scope.isNew = true;
			$scope
				.$evalAsync($http
					.get(shAPIServerService.get().concat("/v2/provider/auth/model"))
					.then(
						function (response) {
							$scope.authProvider = response.data;
							$scope.authProvider.name = "Untitled Auth Provider"
						}));
		}

		$scope.authProviderSave = function () {
			if ($scope.isNew) {
				shAuthProviderResource.save($scope.authProvider, function (response) {
					$scope.authProvider = response;
					$scope.isNew = false;
					Notification.warning($scope.authProvider.name + '  Auth Provider was saved.');
				});
			}
			else {
				$scope.authProvider.$update(function () {
					Notification.warning('The ' + $scope.authProvider.name + ' Auth Provider was updated.');
				});
			}
		}
		$scope.authProviderSaveAndClose = function () {
			$scope.authProviderSave();
			$state.go('config.auth-providers', {}, { reload: true });
		}

		$scope.removeAuthProvider = function () {
			shAuthProviderFactory.delete($scope.authProvider);
		}

	}]);
