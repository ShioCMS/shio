shioApp.controller('ShWidgetComboBoxCtrl', [
	'$scope',
	'Upload',
	'$timeout',
	'$uibModal',
	'shWidgetRelatorFactory',
	'$filter',
	function ($scope, Upload, $timeout, $uibModal, shWidgetRelatorFactory,
		$filter) {
		$scope.widgetSettingsObject = null;
		$scope.choices = [];

		$scope.init = function (widgetSettings) {
			$scope.widgetSettingsObject = angular.fromJson(widgetSettings);
			var choicesArray = $scope.widgetSettingsObject.choices.replace(/(\r\n\n\r)/gm, "\n").split("\n");
			angular.forEach(choicesArray, function (value, key) {
				var choiceRowArray = value.split(":");
				$scope.choices.push({
					id: choiceRowArray[0],
					label: choiceRowArray[1]
				});

			});
		}
	}]);
