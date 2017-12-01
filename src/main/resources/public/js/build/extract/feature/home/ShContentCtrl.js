shioharaApp.controller('ShContentCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"Token",
		function($scope, $http, $window, $state, $rootScope, Token) {
			$scope.accessToken = Token.get();
			$scope.shUser = null;
			$scope.shPosts = null;
			$rootScope.$state = $state;
			$scope.$evalAsync($http.get(
					jp_domain + "/api/user/2?access_token="
							+ $scope.accessToken).then(function(response) {
				$scope.shUser = response.data;
			}));
			$scope.$evalAsync($http.get(jp_domain + "/api/post").then(
					function(response) {
						$scope.shPosts = response.data;
					}));
		} ]);