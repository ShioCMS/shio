shioharaApp.controller('ShWidgetRelatorSelectCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance',
		'shChildrenRelatorItem',
		'shWidgetFileFactory',
		function($scope, shAPIServerService, $http, $uibModalInstance,
				shChildrenRelatorItem,shWidgetFileFactory) {
			var $ctrl = this;
			$scope.shPostAttrs = shChildrenRelatorItem.shChildrenPostAttrs;
			$ctrl.shPostSelected = null;
		
			$scope.shSite = null;
			$scope.shFolders = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;

			// BEGIN Functions
			$ctrl.ok = function() {
				$uibModalInstance.close($ctrl.shPostSelected);
			};

			$ctrl.cancel = function() {
				$ctrl.shPostSelected = null;
				$uibModalInstance.dismiss('cancel');
			};
			
			$scope.selectedPost = function(shPost) {
				$ctrl.shPostSelected = shPost;
			}
			// END Functions

		} ]);
