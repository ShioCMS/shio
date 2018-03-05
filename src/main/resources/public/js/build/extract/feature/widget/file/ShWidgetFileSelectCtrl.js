shioharaApp.controller('ShWidgetFileSelectCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance', 
		'shPost',
		function($scope, shAPIServerService, $http, $uibModalInstance, shPost) {
			var $ctrl = this;
			
			$ctrl.shPost = shPost;
			$ctrl.shPostSelected = null;
			$ctrl.ok = function() {
				$uibModalInstance.close($ctrl.shPostSelected);
			};

			$ctrl.cancel = function() {
				$ctrl.shPostSelected = null;
				$uibModalInstance.dismiss('cancel');
			};
			
			$scope.channelId = $ctrl.shPost.shChannel.id;
			$scope.shSite = null;
			$scope.shChannels = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;

			// BEGIN Functions
			$scope.channelList = function(channelId) {
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/channel/" + channelId + "/list/PT-FILE")).then(
						function(response) {
							$scope.shChannels = response.data.shChannels;
							$scope.shPosts = response.data.shPosts;
							$scope.breadcrumb = response.data.breadcrumb;
							$scope.shSite = response.data.shSite;
						}));
			}
			
			
			$scope.siteList = function(siteId) {
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/site/" + siteId +"/channel"))
						.then(function(response) {
							$scope.shChannels = response.data.shChannels;
							$scope.shPosts = response.data.shPosts;
							$scope.shSite = response.data.shSite;
						}));
			}
			
			$scope.selectedPost = function(shPost) {
				$ctrl.shPostSelected = shPost;
			}
			//END Functions
			
			$scope.channelList($scope.channelId);
			
		
		} ]);