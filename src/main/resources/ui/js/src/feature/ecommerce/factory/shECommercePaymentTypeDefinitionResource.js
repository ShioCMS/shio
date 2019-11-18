shioharaApp.factory('shEcommercePaymentTypeDefinitionResource', [ '$resource', 'shAPIServerService',
		function($resource, shAPIServerService) {
			return $resource(shAPIServerService.get().concat('/v2/ecom/payment/type/definition/:id'), {
				id : '@id'
			}, {
				update : {
					method : 'PUT'
				}
			});
		} ]);
