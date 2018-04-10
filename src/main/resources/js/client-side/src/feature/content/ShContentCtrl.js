shioharaApp.controller('ShContentCtrl', [
		"$scope",
		"$http",
		"$rootScope",
		"$location",
		"shAPIServerService",
		"$state",
		"$window",
		function($scope, $http, $rootScope, $location, shAPIServerService,
				$state,$window) {
			var authenticate = function(credentials) {

				var headers = credentials ? {
					authorization : "Basic "
							+ btoa(credentials.username + ":"
									+ credentials.password)
				} : {};

				$http.get(shAPIServerService.get().concat("/v2"), {
					headers : headers
				}).then(function(response) {
					if (response.data.product) {
						$rootScope.authenticated = true;
						$state.go('content.home');
					} else {
						$rootScope.authenticated = false;
						$state.go('content.login');
					}

				}, function() {
					$rootScope.authenticated = false;
					$state.go('content.login');
				});
			}

			// authenticate();
			if (!$rootScope.authenticated) {
				$http.get(shAPIServerService.get().concat("/v2/user/current")).then(function(response) {
					if (response.data.product) {
						$rootScope.authenticated = true;
						$state.go('content.home');
					} else {
						$rootScope.authenticated = false;
						$state.go('content.login');
					}

				}, function() {
					$rootScope.authenticated = false;
					$state.go('content.login');
				});
			}
			$scope.credentials = {};
			$scope.login = function() {
				authenticate($scope.credentials);
			};

			$rootScope.logout = function() {
				$http.post('logout', {}).then(function() {
					$rootScope.authenticated = false;
					$window.location.href = "/";
				}, function(data) {
					$rootScope.authenticated = false;
				});
			}
		} ]);
