shioharaApp.factory('shEcommercePaymentTypeResource', [ '$resource', 'shAPIServerService',
		function($resource, shAPIServerService) {
			return $resource(shAPIServerService.get().concat('/v2/ecom/payment/type/:id'), {
				id : '@id'
			}, {
				update : {
					method : 'PUT'
				}
			});
		} ]);
