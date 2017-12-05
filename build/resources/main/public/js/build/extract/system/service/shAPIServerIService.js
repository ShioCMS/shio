shioharaApp.service('shAPIServerService', [
		'$http',
		'$location',
		'$cookies',
		function($http, $location, $cookies) {
			var shProtocol = $location.protocol();
			var shHostname = $location.host();
			var shPort = $location.port();
			var shAPIContext = "/api";
			var shEmbServer = shProtocol + "://" + shHostname + ":"
					+ shPort + shAPIContext;
			console.log(shEmbServer);

			this.get = function() {

				if ($cookies.get('shAPIServer') != null)
					return $cookies.get('shAPIServer');
				else {
					$http({
						method : 'GET',
						url : shEmbServer
					}).then(function successCallback(response) {
						$cookies.put('shAPIServer', shEmbServer);
					}, function errorCallback(response) {
						$cookies.put('shAPIServer', 'http://localhost:2710' + shAPIContext);

					});
					return shEmbServer;
				}
			}
		} ]);