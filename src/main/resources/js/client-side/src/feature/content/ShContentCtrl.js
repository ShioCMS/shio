shioharaApp.controller('ShContentCtrl', [ "$rootScope", "$scope","$state",
		'vigLocale', "$translate", "shUserResource", "shPostTypeResource",
		function($rootScope, $scope, $state, vigLocale, $translate, shUserResource, shPostTypeResource) {
			$scope.channelId = null;
			$scope.shUser = null;
			$scope.shSite = null;
			$scope.shPosts = null;
			$scope.shLastPostType = null;
			$scope.shChannels = null;
			$rootScope.$state = $state;
			$scope.breadcrumb = null;
			
			$scope.shUser = shUserResource.get({
				id : 1,
				access_token : $scope.accessToken
			}, function() {
				$scope.shLastPostType = shPostTypeResource.get({
					id : $scope.shUser.lastPostType
				});
				
			});

		} ]);