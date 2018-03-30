shioharaApp
		.controller(
				'ShFolderEditCtrl',
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
							$scope.folderId = $stateParams.folderId;

							$scope.vigLanguage = vigLocale.getLocale()
									.substring(0, 2);
							$translate.use($scope.vigLanguage);
							$scope.shSite = null;
							$scope.shParentFolder = null;
							$scope.shFolder = null;
							$scope.breadcrumb = null;
							$rootScope.$state = $state;
							$scope.shFolder = shFolderResource.get({
								id : $scope.folderId
							});
							rootFolder = false;
							if ($scope.shFolder.rootFolder == 1) {
								rootFolder = true;
							}

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
								$scope.shSite = $scope.shFolder.shSite;
							}
							$scope.folderSave = function() {
								$scope.shFolder
										.$update(function() {
											Notification.warning('The '
													+ $scope.shFolder.name
													+ ' Folder was updated.');
											$state
													.go(
															'content.children.folder-children',
															{
																folderId : $scope.shFolder.parentFolder.id
															});
										});
							}

						} ]);
