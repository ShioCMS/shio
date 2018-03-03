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
		function($scope, $http, $window, $stateParams, $state, $rootScope,
				shPostResource, shAPIServerService, Notification, Upload, $q, $timeout) {
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
				shPostResource
				.delete({
					id : $scope.shPost.id
				},function() {
					 Notification.error('The ' + $scope.shPost.title +' Post was deleted.');
					$state.go('content',{}, {reload: true});
				});
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
				var deferredFile = $q.defer();
				if (shPostAttr.shPostTypeAttr.shWidget.name == "File" && shPostAttr.file != null) {
					var createPost = true;
					if (postType.name == "PT-FILE") {
						createPost = false;
					}
					$scope.numberOfFileWidgets++;
					$scope.file = shPostAttr.file;
					if (!$scope.file.$error) {
						Upload
								.upload(
										{
											url : shAPIServerService
													.get()
													.concat(
															'/staticfile/upload'),
											data : {
												file : $scope.file,
												channelId : $scope.channelId,
												createPost : createPost
											}
										})
								.then(

										function(resp) {

											$scope.filePath = resp.data.title;
										
											if (createPost) {
												$scope.shPost.shPostAttrs[key].strValue = resp.data.id;
												delete $scope.shPost.shPostAttrs[key].referenceObjects;
											} else {
												$scope.shPost.shPostAttrs[key].strValue = $scope.filePath;
											}			
											deferredFile
													.resolve("Success");
											$timeout(function() {
												$scope.log = 'file: '
														+ resp.config.data.file.name
														+ ', Response: '
														+ JSON
																.stringify(resp.data)
														+ '\n'
														+ $scope.log;
											});
										},
										null,
										function(evt) {
											var progressPercentage = parseInt(100.0
													* evt.loaded
													/ evt.total);
											$scope.log = 'progress: '
													+ progressPercentage
													+ '% '
													+ evt.config.data.file.name
													+ '\n'
													+ $scope.log;
										});
					}
				} else {
					deferredFile.resolve("Success");
				}
				return deferredFile.promise;
			}
		} ]);