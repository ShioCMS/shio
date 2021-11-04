shioApp.factory('shWidgetResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/v2/widget/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
