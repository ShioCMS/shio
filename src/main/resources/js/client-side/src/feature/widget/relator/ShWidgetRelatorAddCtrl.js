shioharaApp.controller('ShWidgetRelatorAddCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance',
		'shPostAttr',
		'shWidgetFileFactory',
		function($scope, shAPIServerService, $http, $uibModalInstance,
				shPostAttr, shWidgetFileFactory) {
			$scope.shPostAttrs = [];
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat("/v2/post/attr/model"))
					.then(function(response) {
						$scope.shPostAttrModel = response.data;
						
						$scope.shPostTypeAttrs = shPostAttr.shPostTypeAttr.shPostTypeAttrs;
						
						angular.forEach($scope.shPostTypeAttrs, function(value, key) {
							var attrModel = angular.copy($scope.shPostAttrModel);							
							attrModel.shPostTypeAttr = value;
							this.push(attrModel);
						}, $scope.shPostAttrs);

					}));

			var $ctrl = this;
			
			$ctrl.shPostSelected = null;

			$scope.shSite = null;
			$scope.shFolders = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;

			// BEGIN Functions
			$ctrl.ok = function() {
				$uibModalInstance.close($scope.shPostAttrs);
			};

			$ctrl.cancel = function() {
				$ctrl.shPostSelected = null;
				$uibModalInstance.dismiss('cancel');
			};

			// END Functions

		} ]);
