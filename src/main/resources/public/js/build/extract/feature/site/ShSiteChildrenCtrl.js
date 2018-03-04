shioharaApp.controller('ShSiteChildrenCtrl', [
		"$scope",
		"$http",
		"$state",
		"$stateParams",
		"$rootScope",
		"Token",
		"shUserResource",
		"shPostTypeResource",		
		"shAPIServerService",
		'vigLocale',
		'$translate',
		"shChannelFactory",
		"shPostFactory",
		function($scope, $http, $state, $stateParams, $rootScope, Token,
				shUserResource, shPostTypeResource, shAPIServerService, vigLocale,
				$translate, shChannelFactory, shPostFactory) {
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
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/site/" + $scope.siteId +"/channel"))
					.then(function(response) {
						$scope.shChannels = response.data.shChannels;
						$scope.shPosts = response.data.shPosts;
						$scope.shSite = response.data.shSite;
					}));
			
			$scope.shUser = shUserResource.get({
				id : 1,
				access_token : $scope.accessToken
			}, function() {
				$scope.shLastPostType = shPostTypeResource.get({
					id : $scope.shUser.lastPostType
				});
				
			});
			$scope.channelDelete = function(shChannel) {
				shChannelFactory.delete(shChannel, $scope.shChannels);
			}
			
			$scope.postDelete = function(shPost) {
				shPostFactory.deleteFromList(shPost, $scope.shPosts);
			}
		} ]);