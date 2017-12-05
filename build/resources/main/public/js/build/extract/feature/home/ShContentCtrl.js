shioharaApp.controller('ShContentCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"Token",
		"shUserResource",
		"shPostResource",
		function($scope, $http, $window, $state, $rootScope, Token, shUserResource, shPostResource) {
			$scope.accessToken = Token.get();
			$scope.shUser = null;
			$scope.shPosts = null;
			$rootScope.$state = $state;
			$scope.shUser = shUserResource.get({
				id : 2,
				access_token: $scope.accessToken
			});
			
			$scope.shPosts = shPostResource.query();
		} ]);