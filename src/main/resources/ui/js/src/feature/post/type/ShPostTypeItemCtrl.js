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
			"$filter",
			"shWidgetResource",
			"shPostTypeResource",
			"shPostTypeAttrResource",
			"shAPIServerService",
			"Notification",
			function ($scope, $http, $window, $stateParams, $state,
				$rootScope, $filter, shWidgetResource,
				shPostTypeResource, shPostTypeAttrResource,
				shAPIServerService, Notification) {
				$scope.postTypeId = $stateParams.postTypeId;
				$scope.shPostType = null;
				$scope.shWidgets = shWidgetResource.query();
				$rootScope.$state = $state;

				$scope.updateWidgetSettings = function (shPostType) {
					angular.forEach(shPostType.shPostTypeAttrs, function (shPostTypeAttrChild, key) {
						shPostTypeAttrChild.widgetSettings = JSON.stringify(shPostTypeAttrChild.widgetSettingsObject);
						$scope.updateWidgetSettings(shPostTypeAttrChild);
					});
				}
				$scope.updateWidgetSettingsObject = function (shPostType) {
					angular.forEach(shPostType.shPostTypeAttrs, function (shPostTypeAttrChild, key) {
						shPostTypeAttrChild.widgetSettingsObject = angular.fromJson(shPostTypeAttrChild.widgetSettings);
						$scope.updateWidgetSettingsObject(shPostTypeAttrChild);
					});
				}

				shPostTypeResource
					.get({
							id: $scope.postTypeId
						},
						function (response) {
							// $scope.shPostNewItem =
							// angular.copy($scope.shPostType);
							$scope.shPostType = response;
							$scope.shPostType.shPostTypeAttrs = $filter('orderBy')($scope.shPostType.shPostTypeAttrs, 'ordinal');
							$scope.updateWidgetSettingsObject($scope.shPostType);
						});

				$scope.shPostTypeAttrModel = null;

				$scope
					.$evalAsync($http
						.get(
							shAPIServerService
							.get()
							.concat(
								"/v2/post/type/attr/model"))
						.then(
							function (response) {
								$scope.shPostTypeAttrModel = response.data;
							}));

				$scope.postTypeSaveAndClose = function () {
					$scope.shPostType.$update(function () {
						$scope.updateWidgetSettingsObject($scope.shPostType);
						Notification.warning('The ' + $scope.shPostType.name + ' Post Type was updated.');
						$state.go('content.post-type-select');
					});

				}


				$scope.postTypeSave = function () {
					var log = [];
					$scope.updateWidgetSettings($scope.shPostType);
					$scope.shPostType.$update(function () {
						$scope.shPostType.shPostTypeAttrs = $filter('orderBy')($scope.shPostType.shPostTypeAttrs, 'ordinal');
						$scope.updateWidgetSettingsObject($scope.shPostType);
						Notification.warning('The ' + $scope.shPostType.name + ' Post Type was updated.');
					});

				}
				$scope.removePostType = function () {
					shPostTypeResource
						.delete({
							id: $scope.shPostType.id
						}, function () {
							Notification.error('The ' + $scope.shPostType.name + ' Post Type was deleted.');
							$state.go('content.post-type-select');
						});
				}

				$scope.addPostTypeAttr = function (shWidget, shPostTypeAttrs) {
					$scope.shPostTypeAttrModel.shWidget = shWidget;

					$scope.shPostTypeAttrModel.ordinal = shPostTypeAttrs.length;
					$scope.shPostTypeAttrModel.label = "Field " + (shPostTypeAttrs.length + 1);
					$scope.shPostTypeAttrModel.name = "FIELD_" + (shPostTypeAttrs.length + 1);
					delete $scope.shPostTypeAttrModel.id;
					shPostTypeAttrs.push(angular
						.copy($scope.shPostTypeAttrModel));
				}

				$scope.postTypeAttrDelete = function (shPostTypeAttrs, shPostTypeAttr, index) {
					var index = shPostTypeAttrs
						.indexOf(shPostTypeAttr);
					shPostTypeAttrs.splice(
						index, 1);

				}
			}
		]);