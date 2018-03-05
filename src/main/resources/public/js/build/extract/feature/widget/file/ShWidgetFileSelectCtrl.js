shioharaApp.controller('ShWidgetFileSelectCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance', 
		'shPost',
		function($scope, shAPIServerService, $http, $uibModalInstance, shPost) {
			var $ctrl = this;
			$ctrl.removeInstance = false;
			$ctrl.shPost = shPost;
			$ctrl.ok = function() {
				$ctrl.removeInstance = true;
				$uibModalInstance.close($ctrl.removeInstance);
			};

			$ctrl.cancel = function() {
				$ctrl.removeInstance = false;
				$uibModalInstance.dismiss('cancel');
			};
			
			$scope.channelId = $ctrl.shPost.shChannel.id;
			$scope.shSite = null;
			$scope.shChannels = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;

			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/channel/" + $scope.channelId + "/list")).then(
					function(response) {
						$scope.shChannels = response.data.shChannels;
						$scope.shPosts = response.data.shPosts;
						$scope.breadcrumb = response.data.breadcrumb;
						$scope.shSite = response.data.shSite;
					}));
		} ]);