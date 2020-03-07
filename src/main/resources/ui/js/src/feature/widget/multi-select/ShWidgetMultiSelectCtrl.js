shioApp.controller('ShWidgetMultiSelectCtrl', [
		'$scope',
		'$http',
		'$timeout',
		'$interval',
		'shPostTypeResource',
		'shAPIServerService',
		function($scope, $http, $timeout, $interval, shPostTypeResource, shAPIServerService) {
			var vm = this;
			$scope.widgetSettingsObject = null;
			$scope.choices = [];
			vm.postType = null;
			vm.folderId = null;
			vm.fieldValue = null;
			vm.fieldName = null;
			vm.posts = [];
			vm.init = function(widgetSettings) {
				$scope.widgetSettingsObject = angular.fromJson(widgetSettings);
				vm.postType = $scope.widgetSettingsObject.multiselect.postType;
				vm.folderId = $scope.widgetSettingsObject.multiselect.folderId;
				vm.fieldValue = $scope.widgetSettingsObject.multiselect.value;
				vm.fieldName = $scope.widgetSettingsObject.multiselect.name;

				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/object/" + vm.folderId
										+ "/list/" + vm.postType)).then(
						function(response) {
							vm.posts = response.data.shPosts;
						}));
			}

			vm.msdisabled = false;

			vm.selectedPosts = [];
		} ]);