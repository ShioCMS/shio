shioharaApp.controller('ShWidgetPaymentTypeCtrl', [
		'$scope',
		'$http',
		'shAPIServerService',
		function($scope, $http, shAPIServerService) {
			$scope.paymentTypeMap = [];
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/v2/post/post-type/PT_PAYMENT_TYPE")).then(
					function(response) {
						$scope.paymentTypes = response.data;
						angular.forEach($scope.paymentTypes, function(value, key) {
							$scope.paymentTypeMap[value.id] = value;							
							});
					}));
			$scope.getPaymentType = function(id) {
				return $scope.paymentTypeMap[id];
			}
	
			$scope.addPaymentType = function (paymentType,shPostTypeAttr) {		
				if (shPostTypeAttr.widgetSettingsObject === undefined || shPostTypeAttr.widgetSettingsObject === null) {				
					shPostTypeAttr.widgetSettingsObject = {};
				}
				if (shPostTypeAttr.widgetSettingsObject.paymentTypes === undefined) {
					shPostTypeAttr.widgetSettingsObject.paymentTypes = [];
				}
				var paymentTypeId = {};
				paymentTypeId.id = paymentType.id;	
				paymentTypeId.name = "PaymentType " + (shPostTypeAttr.widgetSettingsObject.paymentTypes.length + 1) 
				shPostTypeAttr.widgetSettingsObject.paymentTypes.push(paymentTypeId);
			}
			
			$scope.deletePaymentType = function (shPostTypeAttr, index) {	
				if (shPostTypeAttr.widgetSettingsObject === undefined || shPostTypeAttr.widgetSettingsObject === null) {				
					shPostTypeAttr.widgetSettingsObject = {};
				}
				if (shPostTypeAttr.widgetSettingsObject.paymentTypes === undefined) {
					shPostTypeAttr.widgetSettingsObject.paymentTypes = [];
				}
							
				shPostTypeAttr.widgetSettingsObject.paymentTypes.splice(index, 1);				
			}
		} ]);
