shioharaApp.controller('ShWidgetComboBoxCtrl', [
		'$scope',
		'Upload',
		'$timeout',
		'$uibModal',
		'shWidgetRelatorFactory',
		'$filter',
		function($scope, Upload, $timeout, $uibModal, shWidgetRelatorFactory,
				$filter) {
			$scope.widgetSettingsObject = null;
			$scope.choices = [];
			
			$scope.init = function(widgetSettings) {
				$scope.widgetSettingsObject = angular.fromJson(widgetSettings);
				var choicesArray = $scope.widgetSettingsObject.choices.replace(/(\r\n\n\r)/gm, "\n").split("\n");
				angular.forEach(choicesArray, function(value, key) {
					var choiceRowArray = value.split(":");
					var choiceRow = [];
					choiceRow["id"] = choiceRowArray[0];
					choiceRow["label"] = choiceRowArray[1];
					$scope.choices.push(choiceRow);
					
					});			
			}
		} ]);
