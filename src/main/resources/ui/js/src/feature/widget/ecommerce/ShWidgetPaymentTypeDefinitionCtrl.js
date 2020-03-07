shioApp.controller('ShWidgetPaymentTypeDefinitionCtrl', [
		'$scope',
		'$timeout',
		'$uibModal',
		'shEcommercePaymentTypeDefinitionResource',
		'$filter',
		function($scope, $timeout, $uibModal,
				shEcommercePaymentTypeDefinitionResource, $filter) {
			$scope.widgetSettingsObject = null;
			$scope.definitions = shEcommercePaymentTypeDefinitionResource
					.query();	
		} ]);
