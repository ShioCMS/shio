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
							$scope.siteId = $stateParams.siteId;
							$scope.folderId = $stateParams.folderId;
							rootFolder = false;
							if ($scope.siteId != null) {
								rootFolder = true;
							}
							$scope.vigLanguage = vigLocale.getLocale()
									.substring(0, 2);
							$translate.use($scope.vigLanguage);
							$scope.shSite = null;
							$scope.shParentFolder = null;
							$scope.shFolder = null;
							$scope.breadcrumb = null;
							$rootScope.$state = $state;
							$scope.$evalAsync($http.get(
									shAPIServerService.get().concat(
											"/folder/model")).then(
									function(response) {
										$scope.shFolder = response.data;
									}));
							if (!rootFolder) {
								$scope
										.$evalAsync($http
												.get(
														shAPIServerService
																.get()
																.concat(
																		"/folder/"
																				+ $scope.folderId
																				+ "/path"))
												.then(
														function(response) {
															$scope.shParentFolder = response.data.currentFolder
															$scope.breadcrumb = response.data.breadcrumb;
															$scope.shSite = response.data.shSite;
														}));
							} else {
								$scope.shSite = shSiteResource.get({
									id : $scope.siteId
								});
							}
							$scope.folderSave = function() {
								if ($scope.shFolder.id != null
										&& $scope.shFolder.id > 0) {
									$scope.shFolder.$update(function() {
										 Notification.warning('The ' + $scope.shFolder.name +' Folder was created.');
										$state.go('content.children.folder-children', {folderId: $scope.shFolder.id});
									});
								} else {
									delete $scope.shFolder.id;
									if (rootFolder) {
										$scope.shFolder.rootFolder = 1;
										$scope.shFolder.shSite = $scope.shSite;
										shFolderResource
												.save(
														$scope.shFolder,
														function(response) {
															$scope.shFolder = response;
															Notification.warning('The ' + $scope.shFolder.name +' Folder was created.');
															$state.go('content.children.folder-children', {folderId: $scope.shFolder.id});
														});

									} else {
										$scope.shFolder.parentFolder = $scope.shParentFolder;
										shFolderResource
												.save(
														$scope.shFolder,
														function(response) {
															$scope.shFolder = response;
															Notification.warning('The ' + $scope.shFolder.name +' Folder was created.');
															$state.go('content.children.folder-children', {folderId: $scope.shFolder.id});
														});
									}
								}
							}

						} ]);
