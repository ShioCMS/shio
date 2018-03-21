shioharaApp.controller('ShContentHomeCtrl', [ "$rootScope", "$scope", "Token",
		'vigLocale', "$translate",
		function($rootScope, $scope, Token, vigLocale, $translate) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);
			$scope.accessToken = Token.get();

		} ]);