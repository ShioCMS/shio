shioharaApp.controller('ShContentCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"Token",
		"shUserResource",
		"shPostResource",
		"shPostTypeResource",
		'vigLocale',
		'$location',
		"$translate",
		function($scope, $http, $window, $state, $rootScope, Token,
				shUserResource, shPostResource, shPostTypeResource, vigLocale, $location,
				$translate) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);

			$scope.accessToken = Token.get();
			$scope.shUser = null;
			$scope.shPosts = null;
			$scope.shLastPostType = null;
			$rootScope.$state = $state;
			$scope.shUser = shUserResource.get({
				id : 1,
				access_token : $scope.accessToken
			}, function() {
				$scope.shLastPostType = shPostTypeResource.get({
					id : $scope.shUser.lastPostType
				});
			});

			$scope.shPosts = shPostResource.query();
		} ]);