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
		"shFolderFactory",
		"shPostFactory",
		function($scope, $http, $state, $stateParams, $rootScope, Token,
				shUserResource, shPostTypeResource, shAPIServerService, vigLocale,
				$translate, shFolderFactory, shPostFactory) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);
			$scope.siteId = $stateParams.siteId;
			$scope.folderId = null;
			$scope.accessToken = Token.get();
			$scope.shUser = null;
			$scope.shSite = null;
			$scope.shLastPostType = null;
			$scope.shFolders = null;		
			$rootScope.$state = $state;
			$scope.breadcrumb = null;	
			$scope.shStateObjects = [];
			$scope.shObjects = [];
			$scope.actions = [];
			
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/site/" + $scope.siteId +"/folder"))
					.then(function(response) {
						$scope.shFolders = response.data.shFolders;
						$scope.shSite = response.data.shSite;
						$scope.$parent.shSite = $scope.shSite;
						angular.forEach($scope.shFolders, function(shFolder, key) {
							$scope.shStateObjects[shFolder.shGlobalId.id] = false;
							$scope.shObjects[shFolder.shGlobalId.id] = shFolder;
							$scope.actions[shFolder.shGlobalId.id] = false ;
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
			
			$scope.folderDelete = function(shFolder) {
				shFolderFactory.deleteFromList(shFolder, $scope.shFolders);
			}
		} ]);
