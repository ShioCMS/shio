shioApp.controller('ShPostEditCtrl', [
	"$scope",
	"$http",
	"$window",
	"$stateParams",
	"$rootScope",
	"shPostResource",
	"shAPIServerService",
	"Notification",
	"$q",
	"shStaticFileFactory",
	"shPostFactory",
	"$filter",
	"shPostXPResource",
	"shObjectFactory",
	function ($scope, $http, $window, $stateParams, $rootScope,
		shPostResource, shAPIServerService, Notification, $q, shStaticFileFactory, shPostFactory, $filter, shPostXPResource, shObjectFactory) {

		$scope.tinymceOptions = {
			plugins: 'shTinyMCE link image code lists paste advlist anchor autolink autoresize charmap codesample directionality emoticons hr insertdatetime legacyoutput media nonbreaking noneditable pagebreak preview print searchreplace tabfocus table template textpattern toc visualblocks visualchars wordcount',
			relative_urls: false,
			toolbar: ' undo redo | bold italic | alignleft aligncenter alignright | code | numlist bullist a11ycheck anchor charmap codesample ltr rtl | link image media shMailTo shAddImage shAddContent | format emoticons  insertdatetime nonbreaking pagebreak paste | preview print searchreplace | table tabledelete | tableprops tablerowprops tablecellprops | tableinsertrowbefore tableinsertrowafter tabledeleterow | tableinsertcolbefore tableinsertcolafter tabledeletecol | template toc visualblocks visualchars wordcount',
			menubar: 'edit insert tools view format table',
			min_height: 500
		};
		$scope.folderId = null;
		$scope.postId = $stateParams.postId;
		$scope.breadcrumb = null;
		$scope.shSite = null;
		$scope.shFolder = null;
		$scope.tabs = [];
		$scope.allowPublish = false;
		$scope.publishStatus = null;
		$scope.shPostXP = shPostXPResource.get({
			id: $scope.postId
		}, function () {
			$scope.shPost = $scope.shPostXP.shPost;
			$scope.allowPublish = $scope.shPostXP.allowPublish;
			$scope.setPublishStatus();

			angular
				.forEach($scope.shPost.shPostAttrs,
					function (shPostAttr, key) {
						// For example: PAYMENT_DEFINITION
						if (angular.equals(shPostAttr.shPostTypeAttr.shWidget.type, "JSON")) {
							shPostAttr.strValue = JSON.parse(shPostAttr.strValue);
						}
					});

			if ($scope.shPost.shFolder != null) {

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
							function (response) {
								$scope.breadcrumb = response.data.breadcrumb;
								$scope.shFolder = response.data.currentFolder;
								$rootScope.shFolder = $scope.shFolder;
								$scope.shSite = response.data.shSite;
							}
						));
			}

			$scope.tabs = [];
			var tabName = $scope.shPost.shPostType.title;

			angular
				.forEach($filter('orderBy')($scope.shPost.shPostAttrs, 'shPostTypeAttr.ordinal', false),
					function (shPostAttr, key) {

						if (shPostAttr.shPostTypeAttr.shWidget.name === 'Tab') {
							tabName = shPostAttr.shPostTypeAttr.label;
							var tab = {
								ordinal: shPostAttr.shPostTypeAttr.ordinal,
								name: tabName
							}
							$scope.tabs.push(tab);

						} else if (key === 0) {
							var tab = [];
							var tab = {
								ordinal: 0,
								name: tabName
							}
							$scope.tabs.push(tab);
						}
						shPostAttr["tab"] = tabName;
					});

			// /
		});

		$scope.setPublishStatus = function () {
			if (!$scope.shPost.published) {
				$scope.publishStatus = "Unpublished";
			}
			else {
				if ($scope.shPost.publishStatus === 'DRAFT') {
					$scope.publishStatus = "Stale";
				}
				else {
					$scope.publishStatus = "Published";
				}
			}
		}

		$scope.openPreviewURL = function () {
			var link = "/preview#!/" + $scope.shPost.id;
			$window.open(link, "_self");
		}

		$scope.postEditForm = "template/post/form.html";
		$scope.postDelete = function () {
			shPostFactory.delete($scope.shPost);
		}

		var nestedUploadFile = function (promiseFiles, parentOfAttr, postType) {
			if (parentOfAttr.shPostAttrs != null) {
				angular
					.forEach(parentOfAttr.shPostAttrs,
						function (shPostAttrNested, key1) {
							promiseFiles
								.push(uploadFile(
									shPostAttrNested,
									postType));
							nestedUploadFile(promiseFiles, shPostAttrNested, postType);
						});
			}
			else if (parentOfAttr.shChildrenRelatorItems != null) {
				angular
					.forEach(parentOfAttr.shChildrenRelatorItems,
						function (shChildrenRelatorItem, key2) {
							angular
								.forEach(shChildrenRelatorItem.shChildrenPostAttrs,
									function (shChildrenPostAttrNested, key3) {
										promiseFiles
											.push(uploadFile(
												shChildrenPostAttrNested,
												postType));
										nestedUploadFile(promiseFiles, shChildrenPostAttrNested, postType);
									});
						});
			}
		}

		$scope.requestWorkflow = function (publishStatus) {
			console.log("requestWorkflow Process");
			$scope.$evalAsync($http.get(shAPIServerService.get().concat(
				"/v2/object/" + $scope.shPost.id + "/request-workflow/" + publishStatus))
				.then(
					function (response) {
						Notification.warning('Sent the request to publish the ' + $scope.shPost.title + ' Post.');
					}));
		}

		$scope.postSave = function (publishStatus) {
			var promiseFiles = [];
			$scope.filePath = null;
			var postType = $scope.shPost.shPostType;
			nestedUploadFile(promiseFiles, $scope.shPost, postType);

			$q
				.all(promiseFiles)
				.then(
					function (dataThatWasPassed) {
						$scope.shPost.publishStatus = publishStatus;
						shPostResource.update({ id: $scope.shPost.id }, $scope.shPost, function (response) {
							$scope.shPost = response;
							angular
								.forEach($scope.shPost.shPostAttrs,
									function (shPostAttr, key) {
										// For example: PAYMENT_DEFINITION
										if (angular.equals(shPostAttr.shPostTypeAttr.shWidget.type, "JSON")) {
											shPostAttr.strValue = JSON.parse(shPostAttr.strValue);
										}
									});
							// /
							var tabName = $scope.shPost.shPostType.title;
							angular
								.forEach($filter('orderBy')($scope.shPost.shPostAttrs, 'shPostTypeAttr.ordinal', false),
									function (shPostAttr, key) {

										if (shPostAttr.shPostTypeAttr.shWidget.name === 'Tab')
											tabName = shPostAttr.shPostTypeAttr.label;
										shPostAttr["tab"] = tabName;
									});
							// /
							$scope.setPublishStatus();
							Notification.warning('The ' + $scope.shPost.title + ' Post was updated.');
						});
					});

		}

		var uploadFile = function (shPostAttr, postType) {
			var widgetSettingsObject = angular
				.fromJson(shPostAttr.shPostTypeAttr.widgetSettings);

			var shFolderId = $scope.folderId;
			if (widgetSettingsObject != null && widgetSettingsObject.file != null) {
				var folderIdSetting = widgetSettingsObject.file.folderId;
				if (folderIdSetting != null &&
					folderIdSetting.trim().length > 0) {
					shFolderId = folderIdSetting;
				}
			}
			return shStaticFileFactory.uploadFile(
				shFolderId, shPostAttr,
				postType);
		}

		$scope.openProperties = function () {
			shObjectFactory.openProperties($scope.shPost);
		}
	}

]);