shioharaApp.controller('ShContentHomeCtrl', [ "$rootScope", "$scope",
		'vigLocale', "$translate",
		function($rootScope, $scope, vigLocale, $translate) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);

		} ]);
