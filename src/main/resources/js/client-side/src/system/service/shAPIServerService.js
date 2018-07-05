shioharaApp.service('shAPIServerService', [
		'$http'
		, '$location'
		, '$cookies'
		, function ($http, $location, $cookies) {
        var shProtocol = $location.protocol();
        var shHostname = $location.host();
        var shPort = $location.port();
        var shAPIContext = "/api";
        var shEmbServer = shProtocol + "://" + shHostname + ":" + shPort;
        var shEmbAPIServer = shEmbServer + shAPIContext;
        this.server = function () {
            if ($cookies.get('shServer') != null) return $cookies.get('shServer');
            else {
                $cookies.put('shServer', shEmbServer);
                return shEmbServer;
            }
        }
        this.get = function () {
            if ($cookies.get('shAPIServer') != null) return $cookies.get('shAPIServer');
            else {
                $cookies.put('shAPIServer', shEmbAPIServer);
                return shEmbAPIServer;
            }
        }
		}]);

shPreviewApp.service('shAPIServerService', [
				'$http'
				, '$location'
				, '$cookies'
				, function ($http, $location, $cookies) {
        var shProtocol = $location.protocol();
        var shHostname = $location.host();
        var shPort = $location.port();
        var shAPIContext = "/api";
        var shEmbServer = shProtocol + "://" + shHostname + ":" + shPort;
        var shEmbAPIServer = shEmbServer + shAPIContext;
        this.server = function () {
            if ($cookies.get('shServer') != null) return $cookies.get('shServer');
            else {
                $cookies.put('shServer', shEmbServer);
                return shEmbServer;
            }
        }
        this.get = function () {
            if ($cookies.get('shAPIServer') != null) return $cookies.get('shAPIServer');
            else {
                $cookies.put('shAPIServer', shEmbAPIServer);
                return shEmbAPIServer;
            }
        }
				}]);