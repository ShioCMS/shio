shioharaApp.controller('ShDevCtrl', [ "$scope", "shAPIDevServerService",
		function($scope, shAPIDevServerService) {
			shAPIDevServerService.set();
		} ]);
