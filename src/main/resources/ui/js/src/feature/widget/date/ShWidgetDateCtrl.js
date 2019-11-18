shioharaApp
		.controller(
				'ShWidgetDateCtrl',
				[
						'$scope',
						'Upload',
						'$timeout',
						'$uibModal',
						'shWidgetRelatorFactory',
						'$filter',
						function($scope, Upload, $timeout, $uibModal,
								shWidgetRelatorFactory, $filter) {
							$scope.currentDate = function() {
								if ($scope.shPostAttr !== null && $scope.shPostAttr.dateValue !== null) {
									var dateAttr = $scope.shPostAttr.dateValue.split('T');
									var dateAttr = dateAttr[0].split('-');
									$scope.shPostAttr.dateValue = new Date(dateAttr[0], dateAttr[1] - 1, dateAttr[2]);
								}
								else {
									//$scope.shPostAttr.dateValue = new Date();
									$scope.shPostAttr.dateValue = null;
								}
							};
							$scope.currentDate();

							$scope.clear = function() {
								$scope.shPostAttr.dateValue = null;
							};

							$scope.inlineOptions = {
								customClass : getDayClass,
								//minDate : new Date(),
								showWeeks : true
							};

							$scope.dateOptions = {
							//	dateDisabled : disabled,
								formatYear : 'yy',
							//	maxDate : new Date(2020, 5, 22),
							//	minDate : new Date(),
								startingDay : 1
							};

							// Disable weekend selection
							/*function disabled(data) {
								var date = data.date, mode = data.mode;
								return mode === 'day'
										&& (date.getDay() === 0 || date
												.getDay() === 6);
							}

							$scope.toggleMin = function() {
								$scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null
										: new Date();
								$scope.dateOptions.minDate = $scope.inlineOptions.minDate;
							};

							$scope.toggleMin();
*/
							$scope.open1 = function() {
								$scope.popup1.opened = true;
							};

							$scope.open2 = function() {
								$scope.popup2.opened = true;
							};

							$scope.setDate = function(year, month, day) {
								$scope.shPostAttr.dateValue = new Date(year, month, day);
							};

							$scope.formats = [ 'dd-MMMM-yyyy', 'yyyy/MM/dd',
									'dd/MM/yyyy', 'shortDate' ];
							$scope.format = $scope.formats[2];
							$scope.altInputFormats = [ 'M!/d!/yyyy' ];

							$scope.popup1 = {
								opened : false
							};

							$scope.popup2 = {
								opened : false
							};
/*
							var tomorrow = new Date();
							tomorrow.setDate(tomorrow.getDate() + 1);
							var afterTomorrow = new Date();
							afterTomorrow.setDate(tomorrow.getDate() + 1);
							$scope.events = [ {
								date : tomorrow,
								status : 'full'
							}, {
								date : afterTomorrow,
								status : 'partially'
							} ];
*/
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

						} ]);
