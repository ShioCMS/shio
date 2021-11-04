shioApp
	.controller(
		'ShWidgetFileSettingCtrl',
		[
			'$scope',
			'$http',
			'shPostTypeResource',
			function ($scope, $http,shPostTypeResource) {
				$scope.postTypes = shPostTypeResource.query();
				
			}
		]);