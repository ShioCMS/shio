shioharaApp
		.controller(
				'ShFolderNewCtrl',
				[
						"$scope",
						"$http",
						"$window",
						"$state",
						"$stateParams",
						"$rootScope",
						"Token",
						"shFolderResource",
						"shSiteResource",
						"shAPIServerService",
						'vigLocale',
						'$location',
						'$translate',
						'Notification',
						function($scope, $http, $window, $state, $stateParams,
								$rootScope, Token, shFolderResource,
								shSiteResource, shAPIServerService, vigLocale,
								$location, $translate, Notification) {
							$scope.objectId = $stateParams.objectId;
							$scope.vigLanguage = vigLocale.getLocale()
									.substring(0, 2);
							$translate.use($scope.vigLanguage);
							$scope.shSite = null;
							$scope.shParentFolder = null;
							$scope.shFolder = null;
							$scope.breadcrumb = null;
							$rootScope.$state = $state;
							
							$scope.$evalAsync($http.get(shAPIServerService.get().concat("/v2/object/" + $scope.objectId + "/path")).then(function (response) {
						            $scope.breadcrumb = response.data.breadcrumb;
						            $scope.shSite = response.data.shSite;
						    }));
							  
							$scope.$evalAsync($http.get(
									shAPIServerService.get().concat(
											"/v2/folder/model")).then(
									function(response) {
										$scope.shFolder = response.data;
									}));
							$scope.folderSave = function() {
								if ($scope.shFolder.id != null) {
									$scope.shFolder.$update(function() {
										 Notification.warning('The ' + $scope.shFolder.name +' Folder was created.');
										$state.go('content.children', {folderId: $scope.shFolder.parentFolder.shGlobalId.id});
									});
								} else {
									delete $scope.shFolder.id;
									$http.post(
									shAPIServerService.get().concat(
											"/v2/folder/object/" + $scope.objectId), $scope.shFolder).then(
									function(response) {
										$scope.shFolder = response.data;
										$state.go('content.children', {objectId: $scope.objectId});
									});						
								}
							}

						} ]);
