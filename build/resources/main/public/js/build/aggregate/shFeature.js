shioharaApp.controller('ShContentCtrl', [ "$rootScope", "$scope", "Token",
		'vigLocale', "$translate",
		function($rootScope, $scope, Token, vigLocale, $translate) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);
			$scope.accessToken = Token.get();

		} ]);
shioharaApp.controller('ShPostFormCtrl', [ "$scope", "$http", "$window",
		"$stateParams", "$state", "$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postTypeAttrId = $stateParams.postTypeAttrId;
			$scope.shPostTypeItem = angular.copy($scope.shPostType);

		} ]);
shioharaApp.controller('ShPostTypeSelectWithChannelCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$stateParams",
		"$rootScope",
		"shPostTypeResource",
		"shSiteResource",
		"shAPIServerService",
		function($scope, $http, $window, $state, $stateParams, $rootScope,
				shPostTypeResource, shSiteResource, shAPIServerService) {
			$scope.siteId = $stateParams.siteId;
			$scope.channelId = $stateParams.channelId;
			$rootScope.$state = $state;
			$scope.shPostTypes = shPostTypeResource.query();
			$scope.shSite = null;
			$scope.breadcrumb = null;
			if ($scope.channelId != null) {
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/channel/" + $scope.channelId + "/path"))
						.then(function(response) {
							$scope.breadcrumb = response.data.breadcrumb;
							$scope.shSite = response.data.currentChannel.shSite;
						}));
			} else {
				$scope.shSite = shSiteResource.get({
					id : $scope.siteId
				});
			}
		} ]);
shioharaApp.controller('ShPostTypeAttrCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shPostTypeAttrResource",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shPostTypeAttrResource) {
			$scope.postTypeAttrId = $stateParams.postTypeAttrId;
			$scope.shPostTypeAttr = shPostTypeAttrResource.get({
				id : $scope.postTypeAttrId
			});
			$rootScope.$state = $state;
			$scope.postTypeAttrSave = function() {
				$scope.shshPostTypeAttrPost.$update(function() {
					$state.go('content.post-type-item');
				});
			}

		} ]);
shioharaApp.controller('ShPostTypeSelectCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$stateParams",
		"$rootScope",
		"shPostTypeResource",
		"shSiteResource",
		"shAPIServerService",
		function($scope, $http, $window, $state, $stateParams, $rootScope,
				shPostTypeResource, shSiteResource, shAPIServerService) {
			$rootScope.$state = $state;
			$scope.shPostTypes = shPostTypeResource.query();
			$scope.shSite = null;
			$scope.breadcrumb = null;
		} ]);
shioharaApp.controller('ShPostTypeEditorCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"shAPIServerService",
		"shPostTypeResource",
		function($scope, $http, $window, $state, $rootScope,
				shAPIServerService, shPostTypeResource) {
			$rootScope.$state = $state;
			$scope.shPostType = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/post/type/model")).then(
					function(response) {
						$scope.shPostType = response.data;
					}));
			$scope.postTypeSave = function() {
				delete $scope.shPostType.id;
				shPostTypeResource.save($scope.shPostType, function() {
					$state.go('content.post-type-select');
				});
			}
		} ]);
shioharaApp
		.controller(
				'ShPostTypeItemCtrl',
				[
						"$scope",
						"$http",
						"$window",
						"$stateParams",
						"$state",
						"$rootScope",
						"shWidgetResource",
						"shPostTypeResource",
						"shPostTypeAttrResource",
						"shAPIServerService",
						function($scope, $http, $window, $stateParams, $state,
								$rootScope, shWidgetResource,
								shPostTypeResource, shPostTypeAttrResource,
								shAPIServerService) {
							$scope.postTypeId = $stateParams.postTypeId;
							$scope.shPostType = null;
							$scope.shWidgets = shWidgetResource.query();
							$rootScope.$state = $state;
							$scope.shPostType = shPostTypeResource
									.get(
											{
												id : $scope.postTypeId
											},
											function(response) {
												$scope.shPostNewItem = angular
														.copy($scope.shPostType);
												for (var i = 0; i < $scope.shPostNewItem.shPostTypeAttrs.length; i++) {
													$scope.shPostNewItem.shPostTypeAttrs[i]['value'] = 'Novo Valor'
															+ i;
												}
											});

							$scope.shPostTypeAttrModel = null;

							$scope
									.$evalAsync($http
											.get(
													shAPIServerService
															.get()
															.concat(
																	"/post/type/attr/model"))
											.then(
													function(response) {
														$scope.shPostTypeAttrModel = response.data;
													}));

							$scope.postTypeSave = function() {
								angular
										.forEach(
												$scope.shPostType.shPostTypeAttrs,
												function(value, key) {
													if (value.willBeDeleted == 1) {
														shPostTypeAttrResource
																.delete({
																	id : value.id
																});
														var index = $scope.shPostType.shPostTypeAttrs
																.indexOf(value);
														$scope.shPostType.shPostTypeAttrs
																.splice(index,
																		1);
													}
												});

								$scope.shPostType.$update(function() {

									$state.go('content.post-type-select');
								});

							}

							$scope.removePostType = function() {
								shPostTypeResource
								.delete({
									id : $scope.shPostType.id
								},function() {
									$state.go('content.post-type-select');
								});
							}
							
							$scope.addPostTypeAttr = function(shWidget) {
								$scope.shPostTypeAttrModel.shWidget = shWidget;
								delete $scope.shPostTypeAttrModel.id;
								$scope.shPostType.shPostTypeAttrs.push(angular
										.copy($scope.shPostTypeAttrModel));
							}
							
							$scope.removePostTypeAttr = function(shPostTypeAttr) {
								if (shPostTypeAttr.id == null
										|| shPostTypeAttr.id == 0) {
									// Removed from shPostTypeAttrs because is
									// not persisted
									var index = $scope.shPostType.shPostTypeAttrs
											.indexOf(shPostTypeAttr);
									$scope.shPostType.shPostTypeAttrs.splice(
											index, 1);
								} else {
									// Mark to be deleted
									shPostTypeAttr['willBeDeleted'] = 1;
								}
							}
						} ]);
shioharaApp.factory('shPostTypeAttrResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/post/type/attr/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
shioharaApp.factory('shPostTypeResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/post/type/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
shioharaApp.controller('ShPostNewCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shAPIServerService",
		"shPostResource",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shAPIServerService, shPostResource) {
			$scope.channelId = $stateParams.channelId;
			$scope.postTypeId = $stateParams.postTypeId;
			$scope.breadcrumb = null;
			$scope.shPost = null;
			$scope.shChannel = null;
			$scope.shSite = null;
			var channelURL = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/channel/" + $scope.channelId + "/path")).then(
					function(response) {
						$scope.shChannel = response.data.currentChannel
						$scope.breadcrumb = response.data.breadcrumb;
						$scope.shSite = response.data.shSite;
						channelURL = shAPIServerService.server().concat(
								"/sites/"
										+ $scope.shSite.name.replace(new RegExp(" ",
										'g'), "-")
										+ "/default/pt-br"
										+ response.data.channelPath.replace(
												new RegExp(" ", 'g'), "-"));
					}));
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/post/type/" + $scope.postTypeId + "/post/model"))
					.then(function(response) {
						$scope.shPost = response.data;
					}));
			$scope.postEditForm = "template/post/form.html";

			$scope.openPreviewURL = function() {

				if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
					var previewURL = channelURL;
				} else {
					var previewURL = channelURL
							+ $scope.shPost.title.replace(new RegExp(" ", 'g'),
									"-");
				}
				$window.open(previewURL, "_self");
			}

			$scope.postSave = function() {
				if ($scope.shPost.id != null && $scope.shPost.id > 0) {
					$scope.shPost.$update(function() {
						// $state.go('content');
					});
				} else {
					delete $scope.shPost.id;
					$scope.shPost.shChannel = $scope.shChannel;
					shPostResource.save($scope.shPost, function(response) {
						console.log(response);
						$scope.shPost = response;
					});
				}
			}
		} ]);
shioharaApp.factory('shPostResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/post/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
shioharaApp.controller('ShPostEditCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		"shPostResource",
		"shAPIServerService",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shPostResource, shAPIServerService) {
			var channelURL = null;
			$scope.channelId = null;
			$scope.postId = $stateParams.postId;
			$scope.breadcrumb = null;
			$scope.shSite = null;
			$scope.shPost = shPostResource.get({
				id : $scope.postId
			}, function() {
				if ( $scope.shPost.shChannel != null) {
					$scope.channelId = $scope.shPost.shChannel.id;
				$scope
				.$evalAsync($http
						.get(
								shAPIServerService
										.get()
										.concat(
												"/channel/" + $scope.channelId + "/path")
												)
						.then(
								function(response) {
									$scope.breadcrumb = response.data.breadcrumb;
									$scope.shSite = response.data.shSite;
									channelURL = shAPIServerService.server().concat(
											"/sites/" + $scope.shSite.name.replace(new RegExp(" ",
											'g'), "-") + "/default/pt-br" + response.data.channelPath.replace(new RegExp(" ",
															'g'), "-"));
								}
								));
				}
			});
			
	
			
							
			$scope.openPreviewURL = function() {
					if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
						var previewURL = channelURL;
					}
					else {
					   var previewURL = channelURL
									+ $scope.shPost.title.replace(new RegExp(" ",
											'g'), "-");
					}
					 $window.open(previewURL,"_self");
			}
	
			$scope.postEditForm = "template/post/form.html";
			$scope.postDelete = function() {
				shPostResource
				.delete({
					id : $scope.shPost.id
				},function() {
					$state.go('content',{}, {reload: true});
				});
			}
			$scope.postSave = function() {
				$scope.shPost.$update(function() {
					// $state.go('content');
				});
			}
		} ]);
shioharaApp.factory('shWidgetResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/widget/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
shioharaApp.controller('ShOAuth2Ctrl', [ "$scope", "$http", "$window",
		"$state", "$rootScope", "Token",
		function($scope, $http, $window, $state, $rootScope, Token) {
			$scope.accessToken = Token.get();

			$scope.authenticate = function() {
				var extraParams = $scope.askApproval ? {
					approval_prompt : 'force'
				} : {};
				Token.getTokenByPopup(extraParams).then(function(params) {
					// Success getting token from popup.

					// Verify the token before setting it, to avoid the confused
					// deputy problem.
					Token.verifyAsync(params.access_token).then(function(data) {
						$rootScope.$apply(function() {
							$scope.accessToken = params.access_token;
							$scope.expiresIn = params.expires_in;

							Token.set(params.access_token);
						});
					}, function() {
						alert("Failed to verify token.")
					});

				}, function() {
					// Failure getting token from popup.
					alert("Failed to get token from popup.");
				});
			};
		} ]);
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
		function($scope, $http, $window, $state, $stateParams, $rootScope, Token,
				shUserResource, shSiteResource, shChannelResource, shPostTypeResource, shPostResource, shAPIServerService, vigLocale, $location,
				$translate, $filter) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);
			$scope.siteId = $stateParams.siteId;
			$scope.channelId = 0;
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
				});
			}
			
			$scope.postDelete = function(postId) {
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
				});
			}
		} ]);
shioharaApp.factory('shUserResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/user/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
shioharaApp
		.controller(
				'ShChannelNewCtrl',
				[
						"$scope",
						"$http",
						"$window",
						"$state",
						"$stateParams",
						"$rootScope",
						"Token",
						"shChannelResource",
						"shSiteResource",
						"shAPIServerService",
						'vigLocale',
						'$location',
						'$translate',
						function($scope, $http, $window, $state, $stateParams,
								$rootScope, Token, shChannelResource,
								shSiteResource, shAPIServerService, vigLocale,
								$location, $translate) {
							$scope.siteId = $stateParams.siteId;
							$scope.channelId = $stateParams.channelId;
							rootChannel = false;
							if ($scope.siteId != null) {
								rootChannel = true;
							}
							$scope.vigLanguage = vigLocale.getLocale()
									.substring(0, 2);
							$translate.use($scope.vigLanguage);
							$scope.shSite = null;
							$scope.shParentChannel = null;
							$scope.shChannel = null;
							$scope.breadcrumb = null;
							$rootScope.$state = $state;
							$scope.$evalAsync($http.get(
									shAPIServerService.get().concat(
											"/channel/model")).then(
									function(response) {
										$scope.shChannel = response.data;
									}));
							if (!rootChannel) {
								$scope
										.$evalAsync($http
												.get(
														shAPIServerService
																.get()
																.concat(
																		"/channel/"
																				+ $scope.channelId
																				+ "/path"))
												.then(
														function(response) {
															$scope.shParentChannel = response.data.currentChannel
															$scope.breadcrumb = response.data.breadcrumb;
															$scope.shSite = response.data.shSite;
														}));
							} else {
								$scope.shSite = shSiteResource.get({
									id : $scope.siteId
								});
							}
							$scope.channelSave = function() {
								if ($scope.shChannel.id != null
										&& $scope.shChannel.id > 0) {
									$scope.shChannel.$update(function() {
										$state.go('content.children.channel-children', {channelId: $scope.shChannel.id});
									});
								} else {
									delete $scope.shChannel.id;
									if (rootChannel) {

										$scope.shChannel.rootChannel = 1;
										$scope.shChannel.shSite = $scope.shSite;
										shChannelResource
												.save(
														$scope.shChannel,
														function(response) {
															$scope.shChannel = response;
														});

									} else {
										$scope.shChannel.parentChannel = $scope.shParentChannel;
										shChannelResource
												.save(
														$scope.shChannel,
														function(response) {
															$scope.shChannel = response;
															$state.go('content.children.channel-children', {channelId: $scope.shChannel.id});
														});
									}
								}
							}

						} ]);
shioharaApp
		.controller(
				'ShChannelEditCtrl',
				[
						"$scope",
						"$http",
						"$window",
						"$state",
						"$stateParams",
						"$rootScope",
						"Token",
						"shChannelResource",
						"shSiteResource",
						"shAPIServerService",
						'vigLocale',
						'$location',
						'$translate',
						function($scope, $http, $window, $state, $stateParams,
								$rootScope, Token, shChannelResource,
								shSiteResource, shAPIServerService, vigLocale,
								$location, $translate) {
							$scope.channelId = $stateParams.channelId;

							$scope.vigLanguage = vigLocale.getLocale()
									.substring(0, 2);
							$translate.use($scope.vigLanguage);
							$scope.shSite = null;
							$scope.shParentChannel = null;
							$scope.shChannel = null;
							$scope.breadcrumb = null;
							$rootScope.$state = $state;
							$scope.shChannel = shChannelResource.get({
								id : $scope.channelId
							});
							rootChannel = false;
							if ($scope.shChannel.rootChannel == 1) {
								rootChannel = true;
							}

							if (!rootChannel) {
								$scope
										.$evalAsync($http
												.get(
														shAPIServerService
																.get()
																.concat(
																		"/channel/"
																				+ $scope.channelId
																				+ "/path"))
												.then(
														function(response) {
															$scope.shParentChannel = response.data.currentChannel
															$scope.breadcrumb = response.data.breadcrumb;
															$scope.shSite = response.data.shSite;
														}));
							} else {
								$scope.shSite = $scope.shChannel.shSite;
							}
							$scope.channelSave = function() {
								$scope.shChannel
										.$update(function() {
											$state
													.go(
															'content.children.channel-children',
															{
																channelId : $scope.shChannel.id
															});
										});
							}

						} ]);
shioharaApp.controller('ShChannelChildrenCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$stateParams",
		"$rootScope",
		"Token",
		"shUserResource",
		"shChannelResource",
		"shPostResource",
		"shPostTypeResource",
		"shAPIServerService",
		'vigLocale',
		'$location',
		"$translate",
		"$filter",
		function($scope, $http, $window, $state, $stateParams, $rootScope,
				Token, shUserResource, shChannelResource, shPostResource,
				shPostTypeResource, shAPIServerService, vigLocale, $location,
				$translate, $filter) {
			$scope.siteId = $stateParams.siteId;
			$scope.channelId = $stateParams.channelId;
			$scope.$parent.channelId = $stateParams.channelId;
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);

			$scope.shSite = null;
			$scope.shChannels = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;
			$rootScope.$state = $state;
		
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/channel/" + $scope.channelId + "/list")).then(
					function(response) {
						$scope.shChannels = response.data.shChannels;
						$scope.shPosts = response.data.shPosts;
						$scope.breadcrumb = response.data.breadcrumb;
						$scope.$parent.breadcrumb = response.data.breadcrumb;
						$scope.shSite = response.data.shSite;
						$scope.$parent.shSite = $scope.shSite;
					}));
			$scope.channelDelete = function(channelId) {
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
				});
			}
			
			$scope.postDelete = function(postId) {
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
				});
			}

		} ]);
shioharaApp.factory('shChannelResource', [ '$resource', 'shAPIServerService',
		function($resource, shAPIServerService) {
			return $resource(shAPIServerService.get().concat('/channel/:id'), {
				id : '@id'
			}, {
				update : {
					method : 'PUT'
				}
			});
		} ]);
shioharaApp.controller('ShSiteListCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"shSiteResource",
		function($scope, $http, $window, $state, $rootScope, shSiteResource) {
			$rootScope.$state = $state;
			$scope.shSites = shSiteResource.query();
		} ]);
shioharaApp.controller('ShSiteEditCtrl', [
		"$scope",
		"$http",
		"$state",
		"$stateParams",
		"$rootScope",
		"shSiteResource",
		"shAPIServerService",
		function($scope, $http, $state, $stateParams, $rootScope,
				shSiteResource, shAPIServerService) {
			$scope.shSite = shSiteResource.get({id: $stateParams.siteId});
			$scope.siteSave = function() {
				$scope.shSite.$update(function() {
					$state.go('content.children.site-children', {
						siteId : $scope.shSite.id
					});
				});
			}
			
			$scope.siteDelete = function() {
				shSiteResource
				.delete({
					id : $scope.shSite.id
				},function() {
					$state.go('content',{}, {reload: true});
				});
			}

		} ]);
shioharaApp.controller('ShSiteNewCtrl', [
		"$scope",
		"$http",
		"$state",
		"$stateParams",
		"$rootScope",
		"shSiteResource",
		"shAPIServerService",
		function($scope, $http, $state, $stateParams, $rootScope, shSiteResource,
				shAPIServerService) {
			$scope.shSite = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/site/model")).then(
					function(response) {
						$scope.shSite = response.data;
					}));
			$scope.siteSave = function() {
				if ($scope.shSite.id != null && $scope.shSite.id > 0) {
					$scope.shSite.$update(function() {
						$state.go('content.children.site-children',{siteId: $scope.shSite.id});
					});
				} else {
					delete $scope.shSite.id;
					shSiteResource.save($scope.shSite, function(response){
						$state.go('content.children.site-children',{siteId: response.id});
					});
				}
			}

		} ]);
shioharaApp.controller('ShSiteChildrenCtrl', [
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
		function($scope, $http, $window, $state, $stateParams, $rootScope, Token,
				shUserResource, shSiteResource, shChannelResource, shPostTypeResource, shPostResource, shAPIServerService, vigLocale, $location,
				$translate, $filter) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);
			$scope.siteId = $stateParams.siteId;
			$scope.channelId = 0;
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
			$scope.channelDelete = function(channelId) {
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
				});
			}
			
			$scope.postDelete = function(postId) {
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
				});
			}
		} ]);
shioharaApp.factory('shSiteResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/site/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);
