shioharaApp.controller('ShWidgetPaymentTypeCtrl', [
		'$scope',
		'$http',
		'shAPIServerService',
		function($scope, $http, shAPIServerService) {
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/v2/post/post-type/PT_PAYMENT_TYPE")).then(
					function(response) {
						$scope.paymentTypes = response.data;
					}));
		} ]);
