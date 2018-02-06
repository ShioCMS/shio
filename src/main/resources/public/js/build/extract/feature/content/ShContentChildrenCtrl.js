shioharaApp.controller('ShContentChildrenCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$stateParams",
		"$rootScope",
		"Token",
		"shUserResource",
		"shSiteResource",
		"shChannelResource",
		"shPostTypeResource",		
		"shPostResource",
		"shAPIServerService",
		'vigLocale',
		'$location',
		'$translate',
		'$filter',
		'Notification',
		function($scope, $http, $window, $state, $stateParams, $rootScope, Token,
				shUserResource, shSiteResource, shChannelResource, shPostTypeResource, shPostResource, shAPIServerService, vigLocale, $location,
				$translate, $filter, Notification) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);
			$scope.siteId = $stateParams.siteId;
			$scope.channelId = null;
			$scope.accessToken = Token.get();
			$scope.shUser = null;
			$scope.shSite = null;
			$scope.shPosts = null;
			$scope.shLastPostType = null;
			$scope.shChannels = null;		
			$rootScope.$state = $state;
			$scope.breadcrumb = null;
			if ($scope.siteId != null) {
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/site/" + $scope.siteId +"/channel"))
					.then(function(response) {
						$scope.shChannels = response.data.shChannels;
						$scope.shPosts = response.data.shPosts;
						$scope.shSite = response.data.shSite;
					}));
			}
			$scope.shUser = shUserResource.get({
				id : 1,
				access_token : $scope.accessToken
			}, function() {
				$scope.shLastPostType = shPostTypeResource.get({
					id : $scope.shUser.lastPostType
				});
				
			});
			$scope.channelDelete = function(channelId) {
				$scope.shChannel = shChannelResource
				.get({
					id : channelId
				});
				shChannelResource
				.delete({
					id : channelId
				},function() {
					// filter the array
				    var foundItem = $filter('filter')($scope.shChannels, { id: channelId  }, true)[0];
				    // get the index
				    var index = $scope.shChannels.indexOf(foundItem );
				    // remove the item from array
				    $scope.shChannels.splice(index, 1);   
					Notification.error('The '
							+ $scope.shChannel.name
							+ ' Channel was deleted.');
				    
				});
			}
			
			$scope.postDelete = function(postId) {
				$scope.shPost = shPostResource
				.get({
					id : postId
				});
				shPostResource
				.delete({
					id : postId
				},function() {
					// filter the array
				    var foundItem = $filter('filter')($scope.shPosts, { id: postId  }, true)[0];
				    // get the index
				    var index = $scope.shPosts.indexOf(foundItem );
				    // remove the item from array
				    $scope.shPosts.splice(index, 1);  
					Notification.error('The '
							+ $scope.shPost.title
							+ ' Post was deleted.');
				});
			}
		} ]);