shioApp
		.controller(
				'ShWidgetContentSelectCtrl',
				[
						'$rootScope',
						'$scope',
						'Upload',
						'$timeout',
						'$uibModal',
						'ShDialogSelectObject',
						function($rootScope, $scope, Upload, $timeout,
								$uibModal, ShDialogSelectObject) {
							$scope.breadcrumbCS = [];
							$scope.init = function(shPostAttr) {
								$scope.updateCSInfo(shPostAttr);
							}
							$scope.folderBreadcrumb = function(breadcrumb,
									shFolder) {
								if (shFolder != null) {
									var breadcrumbItem = {};
									breadcrumbItem.id = shFolder.id;
									breadcrumbItem.name = shFolder.name;
									breadcrumb.push(breadcrumbItem);
									if (shFolder.parentFolder != null) {
										$scope.folderBreadcrumb(breadcrumb,
												shFolder.parentFolder);
									} else if (shFolder.shSite != null) {
										var breadcrumbItem = {};
										breadcrumbItem.id = shFolder.shSite.id;
										breadcrumbItem.name = shFolder.shSite.name
												+ " (Site)";
										breadcrumb.push(breadcrumbItem);
									}
								}
							}
							$scope
									.$watch(
											'shPostAttr.file',
											function() {
												if ($scope.shPostAttr.file != null) {
													$scope.uploadNewFile = false;
													$scope.shPostAttr.strValue = $scope.fileName;
												}
											});

							$scope.updateCSInfo = function(shPostAttr) {
								$scope.breadcrumbCS = [];

								if ((typeof shPostAttr.referenceObject != 'undefined')
										&& (shPostAttr.referenceObject != null)) {
									if (shPostAttr.referenceObject.objectType == "FOLDER") {
										$scope
												.folderBreadcrumb(
														$scope.breadcrumbCS,
														shPostAttr.referenceObject.parentFolder);
									} else if (shPostAttr.referenceObject.objectType == "POST") {
										$scope
												.folderBreadcrumb(
														$scope.breadcrumbCS,
														shPostAttr.referenceObject.shFolder);
									}
								}

							}

							$scope.clearContent = function(shPostAttr) {
								shPostAttr.strValue = null;
								shPostAttr.file = null;
								$scope.breadcrumbCS = [];
							}

							$scope.selectContent = function(shPost, shPostAttr) {

								var widgetSettingsObject = angular
										.fromJson(shPostAttr.shPostTypeAttr.widgetSettings);
								var folderIdSetting = null;
								if (widgetSettingsObject != null) {
									var folderIdSetting = widgetSettingsObject.contentSelect.folderId;
								}
								var shFolderId = null;
								if (folderIdSetting != null
										&& folderIdSetting.trim().length > 0) {
									shFolderId = folderIdSetting;
								} else {
									shFolderId = $rootScope.shFolder.id;
								}
								var modalInstance = ShDialogSelectObject
										.dialog(shFolderId, "shObject");
								modalInstance.result
										.then(
												function(shObjectSelected) {
													// Selected INSERT
													shPostAttr.strValue = shObjectSelected.id;
													shPostAttr.referenceObject = shObjectSelected;
													$scope
															.updateCSInfo(shPostAttr);
												}, function() {
													// Selected CANCEL
												});

							}

							$scope.selectContentByObject = function(shPostAttr,
									shObject) {
								var modalInstance = ShDialogSelectObject
										.dialog(shObject.id, "shObject");
								modalInstance.result
										.then(
												function(shObjectSelected) {
													// Selected INSERT
													shPostAttr.strValue = shObjectSelected.id;
													shPostAttr.referenceObject = shObjectSelected;
													$scope
															.updateCSInfo(shPostAttr);
												}, function() {
													// Selected CANCEL
												});
							}
						} ]);
