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
			var folderURL = null;
			var folderPath = null;
			$scope.folderId = null;
			$scope.postId = $stateParams.postId;
			$scope.breadcrumb = null;
			$scope.shSite = null;
			$scope.shFolder = null;
			$scope.shPost = shPostResource.get({
				id : $scope.postId
			}, function() {
				if ( $scope.shPost.shFolder != null) {
					$scope.folderId = $scope.shPost.shFolder.id;
				$scope
				.$evalAsync($http
						.get(
								shAPIServerService
										.get()
										.concat(
												"/v2/folder/" + $scope.folderId + "/path")
												)
						.then(
								function(response) {									
									$scope.breadcrumb = response.data.breadcrumb;
									$scope.shFolder = response.data.currentFolder;
									$scope.shSite = response.data.shSite;
									folderPath =  shAPIServerService.server().concat("/v1/store/file_source/" + $scope.shSite.name + response.data.folderPath);
									folderURL = shAPIServerService.server().concat(
											"/v2/sites/" + $scope.shSite.name.replace(new RegExp(" ",
											'g'), "-") + "/default/pt-br" + response.data.folderPath.replace(new RegExp(" ",
															'g'), "-"));
								}
								));
				}
			});
			
	
			
							
			$scope.openPreviewURL = function() {
				 var link = shAPIServerService.get().concat("/v2/object/" + $scope.shPost.id + "/preview");
		         $window.open(link,"_self");
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
						$scope.folderId, shPostAttr, key,
						postType, $scope.shPost,
						$scope.numberOfFileWidgets);
			}
		} ]);
