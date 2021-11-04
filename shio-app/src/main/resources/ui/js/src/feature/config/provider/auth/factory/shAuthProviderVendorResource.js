shioApp.factory('shAuthProviderVendorResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/v2/provider/auth/vendor/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
