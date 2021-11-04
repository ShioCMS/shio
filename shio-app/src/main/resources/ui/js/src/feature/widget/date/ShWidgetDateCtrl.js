shioApp
	.controller(
		'ShWidgetDateCtrl',
		['$scope',
			function ($scope) {
				$scope.currentDate = function () {
					if ($scope.shPostAttr !== null && $scope.shPostAttr.dateValue !== null) {
						var dateAttr = $scope.shPostAttr.dateValue.split('T');
						dateAttr = dateAttr[0].split('-');
						$scope.shPostAttr.dateValue = new Date(dateAttr[0], dateAttr[1] - 1, dateAttr[2]);
					}
					else {
						$scope.shPostAttr.dateValue = null;
					}
				};
				$scope.currentDate();

				$scope.clear = function () {
					$scope.shPostAttr.dateValue = null;
				};

				$scope.inlineOptions = {
					customClass: getDayClass,
					showWeeks: true
				};

				$scope.dateOptions = {
					formatYear: 'yy',
					startingDay: 1
				};
				$scope.open1 = function () {
					$scope.popup1.opened = true;
				};

				$scope.open2 = function () {
					$scope.popup2.opened = true;
				};

				$scope.setDate = function (year, month, day) {
					$scope.shPostAttr.dateValue = new Date(year, month, day);
				};

				$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd',
					'dd/MM/yyyy', 'shortDate'];
				$scope.format = $scope.formats[2];
				$scope.altInputFormats = ['M!/d!/yyyy'];

				$scope.popup1 = {
					opened: false
				};

				$scope.popup2 = {
					opened: false
				};

				function getDayClass(data) {
					var date = data.date, mode = data.mode;
					if (mode === 'day') {
						var dayToCheck = new Date(date).setHours(0,
							0, 0, 0);

						for (var i = 0; i < $scope.events.length; i++) {
							var currentDay = new Date(
								$scope.events[i].date)
								.setHours(0, 0, 0, 0);

							if (dayToCheck === currentDay) {
								return $scope.events[i].status;
							}
						}
					}

					return '';
				}

			}]);
