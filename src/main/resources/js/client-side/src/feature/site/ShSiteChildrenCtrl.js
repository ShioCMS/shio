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
			$scope.shLastPostType = null;
			$scope.shChannels = null;		
			$rootScope.$state = $state;
			$scope.breadcrumb = null;	
			$scope.shStateObjects = [];
			$scope.shObjects = [];
			$scope.actions = [];
			
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/site/" + $scope.siteId +"/channel"))
					.then(function(response) {
						$scope.shChannels = response.data.shChannels;
						$scope.shSite = response.data.shSite;
						$scope.$parent.shSite = $scope.shSite;
						angular.forEach($scope.shChannels, function(shChannel, key) {
							$scope.shStateObjects[shChannel.shGlobalId.id] = false;
							$scope.shObjects[shChannel.shGlobalId.id] = shChannel;
							$scope.actions[shChannel.shGlobalId.id] = false ;
						});
					}));
			
			$scope.shUser = shUserResource.get({
				id : 1,
				access_token : $scope.accessToken
			}, function() {
				$scope.shLastPostType = shPostTypeResource.get({
					id : $scope.shUser.lastPostType
				});
				
			});
			
			$scope.updateAction = function(shGlobalId, value) {
				$scope.actions[shGlobalId.id]=value;
			}
			
			$scope.channelDelete = function(shChannel) {
				shChannelFactory.deleteFromList(shChannel, $scope.shChannels);
			}
		} ]);