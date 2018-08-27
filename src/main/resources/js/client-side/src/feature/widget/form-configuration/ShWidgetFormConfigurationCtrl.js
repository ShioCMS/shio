shioharaApp
		.controller(
				'ShWidgetFormConfigurationCtrl',
				[
						'$scope',
						'$http',
						'$q',
						'shAPIServerService',
						'shPostTypeResource',
						function($scope, $http, $q, shAPIServerService,
								shPostTypeResource) {
							$scope.pageLayouts = null;
							$scope.pageLayoutMap = [];
							$scope.shPostTypeAttrForm = null;
							$scope.init = function(shPostTypeAttr) {
								$scope.shPostTypeAttrForm = shPostTypeAttr;
								if ($scope.shPostTypeAttrForm.widgetSettingsObject === undefined
										|| $scope.shPostTypeAttrForm.widgetSettingsObject === null) {
									$scope.shPostTypeAttrForm.widgetSettingsObject = [];
								}
								if ($scope.shPostTypeAttrForm.widgetSettingsObject.form === undefined
										|| $scope.shPostTypeAttrForm.widgetSettingsObject.form === null) {
									$scope.shPostTypeAttrForm.widgetSettingsObject.form = [];
								}
								if ($scope.shPostTypeAttrForm.widgetSettingsObject.form.method === undefined) {
									$scope.shPostTypeAttrForm.widgetSettingsObject.form.method = "POST";
								}

							}

							$scope
									.$evalAsync($http
											.get(
													shAPIServerService
															.get()
															.concat(
																	"/v2/post/post-type/PT-PAGE-LAYOUT"))
											.then(
													function(response) {
														$scope.pageLayouts = response.data;
														angular
																.forEach(
																		$scope.paymentTypes,
																		function(
																				value,
																				key) {
																			$scope.pageLayoutMap[value.id] = value;
																		});
													}));
						} ]);
