shioharaApp.controller('ShPostTypeSelectCtrl',
		[
				"$scope",
				"$http",
				"$window",
				"$state",
				"$stateParams",
				"$rootScope",
				"shPostTypeResource",
				"shAPIServerService",
				function($scope, $http, $window, $state, $stateParams, $rootScope,
						shPostTypeResource, shAPIServerService) {
					$scope.channelId = $stateParams.channelId;
					$rootScope.$state = $state;
					$scope.shPostTypes = shPostTypeResource.query();
					$scope.breadcrumb = null;
					$scope.$evalAsync($http.get(
							shAPIServerService.get().concat(
									"/channel/" + $scope.channelId + "/path"))
							.then(function(response) {
								$scope.breadcrumb = response.data.breadcrumb;
							}));
				} ]);