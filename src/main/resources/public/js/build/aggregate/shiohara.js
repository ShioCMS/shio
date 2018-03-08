var shioharaApp = angular.module('shioharaApp', [ 'ngCookies', 'ngResource',
		'ngAnimate', 'ngSanitize', 'ui.router', 'ui.bootstrap',
		'pascalprecht.translate', 'vecchioOauth', 'angularMoment', 'ui.ace',
		'ngFileUpload', 'ui-notification', 'ui.tinymce' ]);
shioharaApp.config([
		'$stateProvider',
		'$urlRouterProvider',
		'TokenProvider',
		'$locationProvider',
		'$translateProvider',
		'NotificationProvider',
		function($stateProvider, $urlRouterProvider, TokenProvider,
				$locationProvider, $translateProvider, NotificationProvider) {
			
			NotificationProvider.setOptions({
				delay : 5000,
				startTop : 20,
				startRight : 10,
				verticalSpacing : 20,
				horizontalSpacing : 20,
				positionX : 'right',
				positionY : 'bottom'
			});
			TokenProvider.extendConfig({
				clientId : 'b0ec29dd6e0c6bd98b37fee1799dc0a8',
				redirectUri : 'http://localhost:8080/oauth2callback.html',
				scopes : [ "https://www.googleapis.com/auth/userinfo.email" ]
			});

			$translateProvider.useSanitizeValueStrategy('escaped');
			$translateProvider.translations('en', {
				SEARCH : "Search",
				SEARCH_FOR : "Search for"
			});
			$translateProvider.translations('pt', {
				SEARCH : "Pesquisar",
				SEARCH_FOR : "Pesquisar por"
			});

			$translateProvider.fallbackLanguage('en');

			$urlRouterProvider.otherwise('/content');

			$stateProvider.state('oauth2', {
				url : '/oauth2',
				templateUrl : 'template/oauth2.html',
				controller : 'ShOAuth2Ctrl',
				data : {
					pageTitle : 'OAuth2 | Viglet Shiohara'
				}
			}).state('content', {
				url : '/content',
				templateUrl : 'template/content.html',
				controller : 'ShContentCtrl',
				data : {
					pageTitle : 'Content | Viglet Shiohara'
				}
			}).state('content.children', {
				url : '/list',
				templateUrl : 'template/content/content-children.html',
				controller : 'ShContentChildrenCtrl',
				data : {
					pageTitle : 'Content | Viglet Shiohara'
				}
			}).state('content.children.site-children', {
				url : '/site/:siteId',
				templateUrl : 'template/channel/channel-children.html',
				controller : 'ShSiteChildrenCtrl',
				data : {
					pageTitle : 'Content | Viglet Shiohara'
				}
			}).state('content.children.channel-children', {
				url : '/channel/:channelId',
				templateUrl : 'template/channel/channel-children.html',
				controller : 'ShChannelChildrenCtrl',
				data : {
					pageTitle : 'Content | Viglet Shiohara'
				}
			}).state('content.post-type-editor', {
				url : '/post/type/editor',
				templateUrl : 'template/post/type/editor.html',
				controller : 'ShPostTypeEditorCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shiohara'
				}
			}).state('content.post-type-item', {
				url : '/post/type/:postTypeId',
				templateUrl : 'template/post/type/item.html',
				controller : 'ShPostTypeItemCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shiohara'
				}
			}).state('content.post-type-item.attribute', {
				url : '/attrib/:postTypeAttrId',
				templateUrl : 'template/post/type/attribute.html',
				controller : 'ShPostTypeAttrCtrl',
				data : {
					pageTitle : 'Post Type Editor | Viglet Shiohara'
				}
			}).state('content.post-type-item.post-item-new', {
				url : '/channel/:channelId',
				templateUrl : 'template/post/item.html',
				controller : 'ShPostNewCtrl',
				data : {
					pageTitle : 'New Post | Viglet Shiohara'
				}
			}).state('content.post-type-item.post-item', {
				url : '/post/:postId',
				templateUrl : 'template/post/item.html',
				controller : 'ShPostEditCtrl',
				data : {
					pageTitle : 'Edit Post | Viglet Shiohara'
				}
			}).state('post-item-form', {
				url : '/post/type/:postTypeId/post/form',
				templateUrl : 'template/post/form.html',
				controller : 'ShPostFormCtrl',
				data : {}
			}).state('content.channel', {
				url : '/channel/:channelId'
			}).state('content.channel.edit', {
				url : '/edit',
				templateUrl : 'template/channel/channel-edit.html',
				controller : 'ShChannelEditCtrl',
				data : {
					pageTitle : 'Edit Channel | Viglet Shiohara'
				}
			}).state('content.site', {
				url : '/site/:siteId'
			}).state('content.site.edit', {
				url : '/edit',
				templateUrl : 'template/site/site-edit.html',
				controller : 'ShSiteEditCtrl',
				data : {
					pageTitle : 'Edit Site | Viglet Shiohara'
				}
			}).state('content.channel.channel-new', {
				url : '/channel',
				templateUrl : 'template/channel/channel-new.html',
				controller : 'ShChannelNewCtrl',
				data : {
					pageTitle : 'New Channel | Viglet Shiohara'
				}
			}).state('content.site.channel-new', {
				url : '/channel',
				templateUrl : 'template/channel/channel-new.html',
				controller : 'ShChannelNewCtrl',
				data : {
					pageTitle : 'New Channel | Viglet Shiohara'
				}
			}).state('content.channel.post-type-select', {
				url : '/post/type',
				templateUrl : 'template/post/type/select-with-channel.html',
				controller : 'ShPostTypeSelectWithChannelCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shiohara'
				}
			}).state('content.post-type-select', {
				url : '/post/type',
				templateUrl : 'template/post/type/select.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shiohara'
				}
			}).state('content.site.post-type-select', {
				url : '/post/type',
				templateUrl : 'template/post/type/select.html',
				controller : 'ShPostTypeSelectCtrl',
				data : {
					pageTitle : 'Post Type Selection | Viglet Shiohara'
				}
			}).state('content.site-new', {
				url : '/site/new',
				templateUrl : 'template/site/site-new.html',
				controller : 'ShSiteNewCtrl',
				data : {
					pageTitle : 'New Site | Viglet Shiohara'
				}
			})

		} ]);
shioharaApp.directive("fileread", [ function() {
	return {
		scope : {
			fileread : "="
		},
		link : function(scope, element, attributes) {
			element.bind("change", function(changeEvent) {
				var reader = new FileReader();
				reader.onload = function(loadEvent) {
					scope.$apply(function() {
						scope.fileread = loadEvent.target.result;
					});
				}
				reader.readAsDataURL(changeEvent.target.files[0]);
			});
		}
	}
} ]);
shioharaApp.factory('vigLocale', [
		'$window',
		function($window) {
			return {
				getLocale : function() {
					var nav = $window.navigator;
					if (angular.isArray(nav.languages)) {
						if (nav.languages.length > 0) {
							return nav.languages[0].split('-').join('_');
						}
					}
					return ((nav.language || nav.browserLanguage
							|| nav.systemLanguage || nav.userLanguage) || '')
							.split('-').join('_');
				}
			}
		} ]);
shioharaApp.service('shAPIServerService', [
		'$http',
		'$location',
		'$cookies',
		function($http, $location, $cookies) {
			var shProtocol = $location.protocol();
			var shHostname = $location.host();
			var shPort = $location.port();
			var shAPIContext = "/api";
			var shEmbServer = shProtocol + "://" + shHostname + ":"
					+ shPort;
			
			var shEmbAPIServer = shEmbServer + shAPIContext;
			console.log(shEmbServer);

			this.server = function() {

				if ($cookies.get('shServer') != null)
					return $cookies.get('shServer');
				else {
					$http({
						method : 'GET',
						url : shEmbAPIServer
					}).then(function successCallback(response) {
						$cookies.put('shServer', shEmbServer);
					}, function errorCallback(response) {
						$cookies.put('shServer', 'http://localhost:2710');

					});
					return shEmbServer;
				}
			}
			this.get = function() {

				if ($cookies.get('shAPIServer') != null)
					return $cookies.get('shAPIServer');
				else {
					$http({
						method : 'GET',
						url : shEmbAPIServer
					}).then(function successCallback(response) {
						$cookies.put('shAPIServer', shEmbAPIServer);
					}, function errorCallback(response) {
						$cookies.put('shAPIServer', 'http://localhost:2710' + shAPIContext);

					});
					return shEmbAPIServer;
				}
			}
		} ]);

shioharaApp.factory('shChannelFactory', [
	'$uibModal','shChannelResource', 'Notification','$filter',
		function($uibModal,shChannelResource, Notification, $filter) {
			return {
				
				deleteFromList : function(shChannel, shChannels) {
					var modalInstance = this.modalDelete(shChannel);
					modalInstance.result.then(function(removeInstance) {
						deletedMessage = 'The '
							+ shChannel.name
							+ ' Channel was deleted.';
						
						shChannelResource
						.delete({
							id : shChannel.id
						},function() {
							// filter the array
						    var foundItem = $filter('filter')(shChannels, { id: shChannel.id }, true)[0];
						    // get the index
						    var index = shChannels.indexOf(foundItem );
						    // remove the item from array
						    shChannels.splice(index, 1);    		
							Notification.error(deletedMessage);
						});
					}, function() {
						// Selected NO
					});
				}, 
				modalDelete: function (shChannel) {
	                var $ctrl = this;
	                return $uibModal.open({
	                    animation: true
	                    , ariaLabelledBy: 'modal-title'
	                    , ariaDescribedBy: 'modal-body'
	                    , templateUrl: 'template/modal/shDeleteObject.html'
	                    , controller: 'ShModalDeleteObjectCtrl'
	                    , controllerAs: '$ctrl'
	                    , size: null
	                    , appendTo: undefined
	                    , resolve: {
	                    	shObject: function () {
	                            return shChannel;
	                        }
	                    }
	                });
	            }						
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

shioharaApp.controller('ShChannelChildrenCtrl', [
		"$scope",
		"$state",
		"$stateParams",
		"$rootScope",
		"$translate",
		"$http",
		"shAPIServerService",
		'vigLocale',
		"shChannelFactory",
		"shPostFactory",
		function($scope, $state, $stateParams, $rootScope, $translate,$http,
				shAPIServerService, vigLocale, shChannelFactory, shPostFactory) {
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
			$scope.channelDelete = function(shChannel) {
				shChannelFactory.deleteFromList(shChannel, $scope.shChannels);
			}
			
			$scope.postDelete = function(shPost) {
				shPostFactory.deleteFromList(shPost, $scope.shPosts);
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
						'Notification',
						function($scope, $http, $window, $state, $stateParams,
								$rootScope, Token, shChannelResource,
								shSiteResource, shAPIServerService, vigLocale,
								$location, $translate, Notification) {
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
											Notification.warning('The '
													+ $scope.shChannel.name
													+ ' Channel was updated.');
											$state
													.go(
															'content.children.channel-children',
															{
																channelId : $scope.shChannel.id
															});
										});
							}

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
						'Notification',
						function($scope, $http, $window, $state, $stateParams,
								$rootScope, Token, shChannelResource,
								shSiteResource, shAPIServerService, vigLocale,
								$location, $translate, Notification) {
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
										 Notification.warning('The ' + $scope.shChannel.name +' Channel was created.');
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
															Notification.warning('The ' + $scope.shChannel.name +' Channel was created.');
															$state.go('content.children.channel-children', {channelId: $scope.shChannel.id});
														});

									} else {
										$scope.shChannel.parentChannel = $scope.shParentChannel;
										shChannelResource
												.save(
														$scope.shChannel,
														function(response) {
															$scope.shChannel = response;
															Notification.warning('The ' + $scope.shChannel.name +' Channel was created.');
															$state.go('content.children.channel-children', {channelId: $scope.shChannel.id});
														});
									}
								}
							}

						} ]);
shioharaApp.controller('ShContentChildrenCtrl', [
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
				$translate,shChannelFactory, shPostFactory ) {
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
			$scope.channelDelete = function(shChannel) {
				shChannelFactory.deleteFromList(shChannel, $scope.shChannels);
			}
			
			$scope.postDelete = function(shPost) {
				shPostFactory.deleteFromList(shPost, $scope.shPosts);
			}
		} ]);
shioharaApp.controller('ShContentCtrl', [ "$rootScope", "$scope", "Token",
		'vigLocale', "$translate",
		function($rootScope, $scope, Token, vigLocale, $translate) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);
			$scope.accessToken = Token.get();

		} ]);
shioharaApp.controller('ShModalDeleteObjectCtrl', [
		"$uibModalInstance",
		"$http",
		"shObject",
		"shAPIServerService",
		"shWidgetFileFactory",
		function($uibModalInstance, $http, shObject, shAPIServerService,
				shWidgetFileFactory) {
			var $ctrl = this;
			$ctrl.removeInstance = false;
			$ctrl.shObject = shObject;
			shObjectType = $ctrl.shObject.shGlobalId.type

			if (angular.equals(shObjectType, 'POST')
					|| angular.equals(shObjectType, 'POSTTYPE')) {
				$ctrl.objectName = shObject.title;
			} else {
				$ctrl.objectName = shObject.name;
			}

			$http.get(
					shAPIServerService.get().concat(
							"/reference/to/" + $ctrl.shObject.shGlobalId.id))
					.then(function(response) {
						$ctrl.objectRefers = response.data;
					});

			$ctrl.ok = function() {
				$ctrl.removeInstance = true;
				$uibModalInstance.close($ctrl.removeInstance);
			};

			$ctrl.cancel = function() {
				$ctrl.removeInstance = false;
				$uibModalInstance.dismiss('cancel');
			};

			$ctrl.selectFile = function() {
				var modalInstance = shWidgetFileFactory.modalSelectFile($ctrl.shObject.shChannel.id);
				modalInstance.result.then(function(shPostSelected) {
					// Selected INSERT
					console.log("shPostSelected: " + shPostSelected.id);
				}, function() {
					// Selected CANCEL
				});
			}
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
shioharaApp.factory('shPostFactory', [
	'$uibModal', 'shPostResource', 'Notification', '$filter', "$state"



    
    , function ($uibModal, shPostResource, Notification, $filter, $state) {
        return {
            deleteFromList: function (shPost, shPosts) {
                var modalInstance = this.modalDelete(shPost);
                modalInstance.result.then(function (removeInstance) {
                    var deletedMessage = 'The ' + shPost.title + ' Post was deleted.';
                    shPostResource.delete({
                        id: shPost.id
                    }, function () {
                        // filter the array
                        var foundItem = $filter('filter')(shPosts, {
                            id: shPost.id
                        }, true)[0];
                        // get the index
                        var index = shPosts.indexOf(foundItem);
                        // remove the item from array
                        shPosts.splice(index, 1);
                        Notification.error(deletedMessage);
                    });
                }, function () {
                    // Selected NO
                });
            }
            , delete: function (shPost) {
                var modalInstance = this.modalDelete(shPost);
                modalInstance.result.then(function (removeInstance) {
                    deletedMessage = 'The ' + shPost.title + ' Post was deleted.';
                    shPostResource.delete({
                        id: shPost.id
                    }, function () {
                        Notification.error(deletedMessage);
                        $state.go('content.children.channel-children', {
                            channelId: shPost.shChannel.id
                        });
                    });
                }, function () {
                    // Selected NO
                });
            }
            , modalDelete: function (shPost) {
                var $ctrl = this;
                return $uibModal.open({
                    animation: true
                    , ariaLabelledBy: 'modal-title'
                    , ariaDescribedBy: 'modal-body'
                    , templateUrl: 'template/modal/shDeleteObject.html'
                    , controller: 'ShModalDeleteObjectCtrl'
                    , controllerAs: '$ctrl'
                    , size: null
                    , appendTo: undefined
                    , resolve: {
                    	shObject: function () {
                            return shPost;
                        }
                    }
                });
            }
        }
		}]);
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
		"Notification",
		"Upload",
		"$q",
		"$timeout",
		"shStaticFileFactory",
		"shPostFactory",
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shPostResource, shAPIServerService, Notification, Upload, $q, $timeout, shStaticFileFactory, shPostFactory) {
			$scope.tinymceOptions = {
				    plugins: 'link image code',
				    toolbar: 'undo redo | bold italic | alignleft aligncenter alignright | code'
				  };
			var channelURL = null;
			var folderPath = null;
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
									folderPath =  shAPIServerService.server().concat("/store/file_source/" + $scope.shSite.name + response.data.channelPath);
									channelURL = shAPIServerService.server().concat(
											"/sites/" + $scope.shSite.name.replace(new RegExp(" ",
											'g'), "-") + "/default/pt-br" + response.data.channelPath.replace(new RegExp(" ",
															'g'), "-"));
								}
								));
				}
			});
			
	
			
							
			$scope.openPreviewURL = function() {
				if ($scope.shPost.shPostType.name == 'PT-FILE') {
					var previewURL = folderPath + $scope.shPost.title;
				}
				else if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
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
				shPostFactory.delete($scope.shPost);
			}
			$scope.postSave = function() {
				var promiseFiles = [];

				$scope.filePath = null;
				$scope.numberOfFileWidgets = 0;
				var postType = $scope.shPost.shPostType;
				angular
						.forEach($scope.shPost.shPostAttrs,
								function(shPostAttr, key) {
									promiseFiles
											.push(uploadFile(
													shPostAttr,
													key,
													postType));
								});

				$q
						.all(promiseFiles)
						.then(
								function(dataThatWasPassed) {
				$scope.shPost.$update(function() {
					 Notification.warning('The ' + $scope.shPost.title +' Post was updated.');
				});
								});
				
			}
			
			var uploadFile = function(shPostAttr, key, postType) {
				return shStaticFileFactory.uploadFile(
						$scope.channelId, shPostAttr, key,
						postType, $scope.shPost,
						$scope.numberOfFileWidgets);
			}
		} ]);
shioharaApp.controller('ShPostFormCtrl', [ "$scope", "$http", "$window",
		"$stateParams", "$state", "$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postTypeAttrId = $stateParams.postTypeAttrId;
			$scope.shPostTypeItem = angular.copy($scope.shPostType);

		} ]);
shioharaApp
		.controller(
				'ShPostNewCtrl',
				[
						"$scope",
						"$http",
						"$window",
						"$stateParams",
						"$state",
						"$rootScope",
						"shAPIServerService",
						"shPostResource",
						"Notification",
						"Upload",
						"$q",
						"$timeout",
						"shStaticFileFactory",
						function($scope, $http, $window, $stateParams, $state,
								$rootScope, shAPIServerService, shPostResource,
								Notification, Upload, $q, $timeout,
								shStaticFileFactory) {
							$scope.tinymceOptions = {
								plugins : 'link image code',
								toolbar : 'undo redo | bold italic | alignleft aligncenter alignright | code'
							};
							$scope.channelId = $stateParams.channelId;
							$scope.postTypeId = $stateParams.postTypeId;
							$scope.breadcrumb = null;
							$scope.shPost = null;
							$scope.shChannel = null;
							$scope.shSite = null;
							var channelURL = null;
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
														$scope.shChannel = response.data.currentChannel
														$scope.breadcrumb = response.data.breadcrumb;
														$scope.shSite = response.data.shSite;
														folderPath = shAPIServerService
																.server()
																.concat(
																		"/store/file_source/"
																				+ $scope.shSite.name
																				+ response.data.channelPath);
														channelURL = shAPIServerService
																.server()
																.concat(
																		"/sites/"
																				+ $scope.shSite.name
																						.replace(
																								new RegExp(
																										" ",
																										'g'),
																								"-")
																				+ "/default/pt-br"
																				+ response.data.channelPath
																						.replace(
																								new RegExp(
																										" ",
																										'g'),
																								"-"));
													}));
							$scope.$evalAsync($http.get(
									shAPIServerService.get().concat(
											"/post/type/" + $scope.postTypeId
													+ "/post/model")).then(
									function(response) {
										$scope.shPost = response.data;
									}));
							$scope.postEditForm = "template/post/form.html";

							$scope.openPreviewURL = function() {
								if ($scope.shPost.shPostType.name == 'PT-FILE') {
									var previewURL = folderPath
											+ $scope.shPost.title;
								} else if ($scope.shPost.shPostType.name == 'PT-CHANNEL-INDEX') {
									var previewURL = channelURL;
								} else {
									var previewURL = channelURL
											+ $scope.shPost.title.replace(
													new RegExp(" ", 'g'), "-");
								}
								$window.open(previewURL, "_self");
							}

							var uploadFile = function(shPostAttr, key, postType) {
								return shStaticFileFactory.uploadFile(
										$scope.channelId, shPostAttr, key,
										postType, $scope.shPost,
										$scope.numberOfFileWidgets);
							}
							$scope.postSave = function() {

								var promiseFiles = [];

								$scope.filePath = null;
								$scope.numberOfFileWidgets = 0;
								var postType = $scope.shPost.shPostType;
								angular
										.forEach($scope.shPost.shPostAttrs,
												function(shPostAttr, key) {
													promiseFiles
															.push(uploadFile(
																	shPostAttr,
																	key,
																	postType));
												});

								$q
										.all(promiseFiles)
										.then(
												function(dataThatWasPassed) {
													if ($scope.shPost.id != null) {
														$scope.shPost
																.$update(function() {
																	Notification
																			.warning('The '
																					+ $scope.shPost.title
																					+ ' Post was update.');
																	// $state.go('content');
																});
													} else {
														delete $scope.shPost.id;
														$scope.shPost.shChannel = $scope.shChannel;
														shPostResource
																.save(
																		$scope.shPost,
																		function(
																				response) {
																			$scope.shPost = response;
																			Notification
																					.warning('The '
																							+ $scope.shPost.title
																							+ ' Post was created.');
																		});
													}

												});
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
shioharaApp.controller('ShPostTypeEditorCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		"shAPIServerService",
		"shPostTypeResource",
		"Notification",
		function($scope, $http, $window, $state, $rootScope,
				shAPIServerService, shPostTypeResource, Notification) {
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
					Notification.warning('The ' + $scope.shPostType.name +' Site was created.');
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
						"Notification",
						function($scope, $http, $window, $stateParams, $state,
								$rootScope, shWidgetResource,
								shPostTypeResource, shPostTypeAttrResource,
								shAPIServerService, Notification) {
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
									Notification.warning('The ' + $scope.shPostType.name +' Site was updated.');
									$state.go('content.post-type-select');
								});

							}

							$scope.removePostType = function() {
								shPostTypeResource
								.delete({
									id : $scope.shPostType.id
								},function() {
									Notification.error('The ' + $scope.shPostType.name +' Site was deleted.');
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
shioharaApp.factory('shSiteFactory', [
	'$uibModal','shSiteResource', 'Notification','$state',
		function($uibModal,shChannelResource, Notification, $state) {
			return {
				
				delete : function(shSite) {
					var modalInstance = this.modalDelete(shSite);
					modalInstance.result.then(function(removeInstance) {
						deletedMessage = 'The ' + shSite.name +' Site was deleted.';
						
						shSiteResource
						.delete({
							id : shSite.id
						},function() {
							Notification.error(deletedMessage);
							$state.go('content',{}, {reload: true});
						});
					}, function() {
						// Selected NO
					});
				}, 			
				save: function(shSite) {
					if (shSite.id != null) {
						var updateMessage = 'The ' + shSite.name +' Site was saved.';
						shSite.$update(function() {
							Notification.warning(updateMessage);
							$state.go('content.children.site-children',{siteId: shSite.id});						
						});
					} else {
						var saveMessage = 'The ' + shSite.name +' Site was updated.';
						delete $scope.shSite.id;
						shSiteResource.save(shSite, function(response){
							Notification.warning(saveMessage);
							$state.go('content.children.site-children',{siteId: response.id});
						});
					}	
				},
				modalDelete: function (shSite) {
	                var $ctrl = this;
	                return $uibModal.open({
	                    animation: true
	                    , ariaLabelledBy: 'modal-title'
	                    , ariaDescribedBy: 'modal-body'
	                    , templateUrl: 'template/modal/shDeleteObject.html'
	                    , controller: 'ShModalDeleteObjectCtrl'
	                    , controllerAs: '$ctrl'
	                    , size: null
	                    , appendTo: undefined
	                    , resolve: {
	                        shObject: function () {
	                            return shSite;
	                        }
	                    }
	                });
	            }						
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
				shChannelFactory.deleteFromList(shChannel, $scope.shChannels);
			}
			
			$scope.postDelete = function(shPost) {
				shPostFactory.deleteFromList(shPost, $scope.shPosts);
			}
		} ]);
shioharaApp.controller('ShSiteEditCtrl', [
		"$scope",
		"$state",
		"$stateParams",
		"shSiteResource",
		"shSiteFactory",
		function($scope, $state, $stateParams, shSiteResource, shSiteFactory) {
			$scope.shSite = shSiteResource.get({id: $stateParams.siteId});
			$scope.siteSave = function() {
				shSiteFactory.save($scope.shSite);
			}
			
			$scope.siteDelete = function() {
				shSiteFactory.delete($scope.shSite);
			}

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
shioharaApp.controller('ShSiteNewCtrl', [
		"$scope",
		"$http",
		"shAPIServerService",
		"shSiteFactory",
		function($scope, $http, shAPIServerService, shSiteFactory) {
			$scope.shSite = null;
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/site/model")).then(
					function(response) {
						$scope.shSite = response.data;
					}));
			$scope.siteSave = function() {
				shSiteFactory.save($scope.shSite);
			}

		} ]);
shioharaApp.factory('shStaticFileFactory', [
	'$q','Upload', 'shAPIServerService','$timeout',
		function($q,Upload, shAPIServerService,$timeout) {
			return {
				uploadFile : function(channelId, shPostAttr, key, postType, shPost, numberOfFileWidgets ) {
					var deferredFile = $q.defer();
					if (shPostAttr.shPostTypeAttr.shWidget.name == "File"
							&& shPostAttr.file != null) {
						var createPost = true;
						if (postType.name == "PT-FILE") {
							createPost = false;
						}
						numberOfFileWidgets++;
						file = shPostAttr.file;
						if (!file.$error) {
							Upload
									.upload(
											{
												url : shAPIServerService
														.get()
														.concat(
																'/staticfile/upload'),
												data : {
													file : file,
													channelId : channelId,
													createPost : createPost
												}
											})
									.then(

											function(resp) {

												filePath = resp.data.title;
											
												if (createPost) {
													shPost.shPostAttrs[key].strValue = resp.data.id;
													
													delete shPost.shPostAttrs[key].referenceObjects;
												} else {
													shPost.shPostAttrs[key].strValue = filePath;
												}	
												deferredFile
														.resolve("Success");										
											},
											null,
											function(evt) {
												var progressPercentage = parseInt(100.0
														* evt.loaded
														/ evt.total);
											});
						}
					} else {
						deferredFile.resolve("Success");
					}
					return deferredFile.promise;
				}					
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

shioharaApp.factory('shWidgetResource', [ '$resource', 'shAPIServerService', function($resource, shAPIServerService) {
	return $resource(shAPIServerService.get().concat('/widget/:id'), {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);

shioharaApp.factory('shWidgetFileFactory', [ '$uibModal', 'shPostResource',
		'Notification', '$filter', "$state",
		function($uibModal, shPostResource, Notification, $filter, $state) {
			return {
				modalSelectFile : function(channelId) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/widget/file/file-select.html',
						controller : 'ShWidgetFileSelectCtrl',
						controllerAs : '$ctrl',
						size : null,
						appendTo : undefined,
						resolve : {
							channelId : function() {
								return channelId;
							}
						}
					});
				}
			}
		} ]);
shioharaApp.controller('ShWidgetFileCtrl', [
		'$scope',
		'Upload',
		'$timeout',
		'$uibModal',
		'shWidgetFileFactory',
		function($scope, Upload, $timeout, $uibModal, shWidgetFileFactory) {
			$scope.fileName = null;
			$scope.uploadNewFile = false;
			$scope.$watch('shPostAttr.file', function() {
				if ($scope.shPostAttr.file != null) {
					$scope.uploadNewFile = false;
					$scope.fileName = $scope.shPostAttr.file.name;
					$scope.shPostAttr.strValue = $scope.fileName;
				}
			});

			$scope.newFile = function() {
				$scope.uploadNewFile = true;
			}
			$scope.clearFile = function(shPostAttr) {
				shPostAttr.strValue = null;
				shPostAttr.file = null;
			}

			$scope.selectFile = function(shPost, shPostAttr) {
				var modalInstance = shWidgetFileFactory
						.modalSelectFile($scope.channelId);
				modalInstance.result.then(function(shPostSelected) {
					// Selected INSERT
					$scope.uploadNewFile = false;
					$scope.fileName = shPostSelected.title;
					shPostAttr.strValue = shPostSelected.id;
					shPostAttr.referenceObjects = [ shPostSelected ];
				}, function() {
					// Selected CANCEL
				});
			}
		} ]);
shioharaApp.controller('ShWidgetFileSelectCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance',
		'channelId',
		'shWidgetFileFactory',
		function($scope, shAPIServerService, $http, $uibModalInstance,
				channelId,shWidgetFileFactory) {
			var $ctrl = this;

			$ctrl.shPostSelected = null;
			$ctrl.ok = function() {
				$uibModalInstance.close($ctrl.shPostSelected);
			};

			$ctrl.cancel = function() {
				$ctrl.shPostSelected = null;
				$uibModalInstance.dismiss('cancel');
			};
			$scope.shSite = null;
			$scope.shChannels = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;

			// BEGIN Functions
			$scope.channelList = function(channelId) {
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/channel/" + channelId + "/list/PT-FILE"))
						.then(function(response) {
							$scope.shChannels = response.data.shChannels;
							$scope.shPosts = response.data.shPosts;
							$scope.breadcrumb = response.data.breadcrumb;
							$scope.shSite = response.data.shSite;
						}));
			}

			$scope.siteList = function(siteId) {
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/site/" + siteId + "/channel")).then(
						function(response) {
							$scope.shChannels = response.data.shChannels;
							$scope.shPosts = response.data.shPosts;
							$scope.shSite = response.data.shSite;
						}));
			}

			$scope.selectedPost = function(shPost) {
				$ctrl.shPostSelected = shPost;
			}
			// END Functions

			$scope.channelList(channelId);

		} ]);

