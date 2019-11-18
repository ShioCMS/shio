shioharaApp.factory('shUserResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/v2/user/:id'), {
		id : '@username'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
