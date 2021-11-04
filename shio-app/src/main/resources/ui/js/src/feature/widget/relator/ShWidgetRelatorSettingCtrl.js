shioApp.controller('ShWidgetRelatorSettingCtrl', [
		'$scope',
		'$filter',
		function($scope, $filter) {
			$scope.shPostTypeAttr.shPostTypeAttrs = $filter('orderBy')(					
					$scope.shPostTypeAttr.shPostTypeAttrs, 'ordinal');

		} ]);