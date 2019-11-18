shioharaApp
	.controller(
		'ShWidgetContentSelectSettingCtrl',
		[
			'$scope',
			'$http',
			'shPostTypeResource',
			function ($scope, $http,shPostTypeResource) {
				$scope.postTypes = shPostTypeResource.query();
				
			}
		]);