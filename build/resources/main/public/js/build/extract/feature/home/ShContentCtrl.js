shioharaApp.controller('ShContentCtrl', [ "$scope", "Token", 'vigLocale',
		"$translate", function($scope, Token, vigLocale, $translate) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);
			$scope.accessToken = Token.get();
		} ]);