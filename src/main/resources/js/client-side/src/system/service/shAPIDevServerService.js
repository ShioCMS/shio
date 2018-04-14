shioharaApp.service('shAPIDevServerService', [
		'$http',
		'$location',
		'$cookies',
		function($http, $location, $cookies) {
			var shAPIContext = "/api";
			var shEmbServer = 'http://localhost:2710';
			var shEmbAPIServer = shEmbServer + shAPIContext;
			this.set = function() {
				if ($cookies.get('shServer') != shEmbServer) {
					$cookies.put('shServer', shEmbServer);
				}
				if ($cookies.get('shAPIServer') != shEmbAPIServer) {
					$cookies.put('shAPIServer', shEmbAPIServer);
				}
			}
		} ]);
