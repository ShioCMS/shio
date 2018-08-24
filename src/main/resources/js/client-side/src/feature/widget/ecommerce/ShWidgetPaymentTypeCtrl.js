shioharaApp
		.controller(
				'ShWidgetPaymentTypeCtrl',
				[
						'$scope',
						'$http',
						'$q',
						'shAPIServerService',
						'shPostTypeResource',
						function($scope, $http, $q, shAPIServerService,
								shPostTypeResource) {
							$scope.paymentTypeMap = [];
							$scope.postTypeMap = [];
							$scope.productPosts = [];
							$scope.currentProduct = null;
							$scope.loopPromises = [];
							$scope.deferred = $q.defer();
							$scope.loopPromises.push($scope.deferred.promise);
							$scope.shPostTypeAttrPayment = null;
							$scope.init = function(shPostTypeAttr) {
								$scope.shPostTypeAttrPayment = shPostTypeAttr;

							}

							$scope
									.$evalAsync($http
											.get(
													shAPIServerService
															.get()
															.concat(
																	"/v2/post/post-type/PT_PAYMENT_TYPE"))
											.then(
													function(response) {
														$scope.paymentTypes = response.data;
														angular
																.forEach(
																		$scope.paymentTypes,
																		function(
																				value,
																				key) {
																			$scope.paymentTypeMap[value.id] = value;
																		});
													}));
							$scope.postTypes = shPostTypeResource.query();

							$scope.postTypes.$promise.then(function() {
								angular.forEach($scope.postTypes, function(
										value, key) {
									$scope.postTypeMap[value.id] = value;
								});
								$scope.getProductPosts();
							});

							$scope.getProductPosts = function() {
								postTypeId = $scope.shPostTypeAttrPayment.widgetSettingsObject.product.postType;
								if (angular.equals($scope.currentProduct,
										postTypeId)) {
									return $scope.productPosts;
								} else {
									$scope.currentProduct = postTypeId;
									$q
											.all($scope.loopPromises)
											.then(
													$http
															.get(
																	shAPIServerService
																			.get()
																			.concat(
																					"/v2/post/post-type/"
																							+ $scope.postTypeMap[postTypeId].name))
															.then(
																	function(
																			response) {
																		$scope.productPosts = response.data;																	
																		return response.data;
																	}));
								}

							}

							$scope.getPaymentType = function(id) {
								return $scope.paymentTypeMap[id];
							}

							$scope.addPaymentType = function(paymentType,
									shPostTypeAttr) {
								if (shPostTypeAttr.widgetSettingsObject === undefined
										|| shPostTypeAttr.widgetSettingsObject === null) {
									shPostTypeAttr.widgetSettingsObject = {};
								}
								if (shPostTypeAttr.widgetSettingsObject.paymentTypes === undefined) {
									shPostTypeAttr.widgetSettingsObject.paymentTypes = [];
								}
								var paymentTypeId = {};
								paymentTypeId.id = paymentType.id;
								paymentTypeId.name = "PaymentType "
										+ (shPostTypeAttr.widgetSettingsObject.paymentTypes.length + 1)
								shPostTypeAttr.widgetSettingsObject.paymentTypes
										.push(paymentTypeId);
							}

							$scope.deletePaymentType = function(shPostTypeAttr,
									index) {
								if (shPostTypeAttr.widgetSettingsObject === undefined
										|| shPostTypeAttr.widgetSettingsObject === null) {
									shPostTypeAttr.widgetSettingsObject = {};
								}
								if (shPostTypeAttr.widgetSettingsObject.paymentTypes === undefined) {
									shPostTypeAttr.widgetSettingsObject.paymentTypes = [];
								}

								shPostTypeAttr.widgetSettingsObject.paymentTypes
										.splice(index, 1);
							}
						} ]);
