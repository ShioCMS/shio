var shioWelcome = angular.module('shioWelcome',
		[ 'pascalprecht.translate','ngCookies' ]);

shioWelcome
		.config(function($httpProvider) {
			$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
		});

shioWelcome.service('shAPIServerService', [
	'$http',
	'$location',
	'$cookies',
	function($http, $location, $cookies) {
		var shProtocol = $location.protocol();
		var shHostname = $location.host();
		var shPort = $location.port();
		var shAPIContext = "/api";
		var shEmbServer = shProtocol + "://" + shHostname + ":"
				+ shPort;
		
		var shEmbAPIServer = shEmbServer + shAPIContext;
		console.log(shEmbServer);

		this.server = function() {

			if ($cookies.get('shServer') != null)
				return $cookies.get('shServer');
			else {
				$http({
					method : 'GET',
					url : shEmbAPIServer + "/v2"
				}).then(function successCallback(response) {
					$cookies.put('shServer', shEmbServer);
				}, function errorCallback(response) {
					//$cookies.put('shServer', 'http://localhost:2710');
					$cookies.put('shServer', shEmbServer);
				});
				return shEmbServer;
			}
		}
		this.get = function() {

			if ($cookies.get('shAPIServer') != null)
				return $cookies.get('shAPIServer');
			else {
				$http({
					method : 'GET',
					url : shEmbAPIServer + "/v2"
				}).then(function successCallback(response) {
					$cookies.put('shAPIServer', shEmbAPIServer);
				}, function errorCallback(response) {
					//$cookies.put('shAPIServer', 'http://localhost:2710' + shAPIContext);
					$cookies.put('shAPIServer', shEmbAPIServer);
				});
				return shEmbAPIServer;
			}
		}
	} ]);


shioWelcome.controller('ShWelcomeCtrl', [
		"$scope",
		"$http",
		"$window",
		"shAPIServerService",
		function($scope, $http, $window, shAPIServerService) {

			$scope.showLogin = false;

			var errorUI = function() {
				$('.log-status').addClass('wrong-entry');
				$('.alert').fadeIn(500);
				setTimeout("$('.alert').fadeOut(1500);", 3000);
				$('.form-control').keypress(function() {
					$('.log-status').removeClass('wrong-entry');
				});
			}

			var authenticate = function(credentials) {

				var headers = credentials ? {
					authorization : "Basic "
							+ btoa(credentials.username + ":"
									+ credentials.password)
				} : {};

				$http.get(shAPIServerService
						.get()
						.concat("/v2"), {
					headers : headers
				}).then(function(response) {
					if (response.data.product) {
						$scope.showLogin = false;
						$window.location.href = "/content";
					} else {
						$scope.showLogin = true;
						errorUI();
					}
				}, function() {
					$scope.showLogin = true;
					errorUI();
				});
			}

			$scope.credentials = {};
			$scope.login = function() {
				authenticate($scope.credentials);
			};

			// Check Auth
			$http.get(shAPIServerService
					.get()
					.concat("/v2")).then(function(response) {
				if (response.data.product) {
					$scope.showLogin = false;
					$window.location.href = "/content";
				} else {
					$scope.showLogin = true;
				}
			}, function() {
				$scope.showLogin = true;
			});
		} ]);
