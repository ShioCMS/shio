shioharaApp.controller('ShComponentExplorerCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance',
		'channelId',
		'objectType',
		'shWidgetFileFactory',
		function($scope, shAPIServerService, $http, $uibModalInstance,
				channelId, objectType, shWidgetFileFactory) {
			var $ctrl = this;
			$ctrl.objectTypeName = "Object";
			$ctrl.channelDoubleClick = false;
			if (angular.equals(objectType, "shChannel")) {
				$ctrl.objectTypeName = "Channel";
				$ctrl.channelDoubleClick = true;
			}
			$ctrl.enableInsertButton = false;
			 $ctrl.shChannelSelected == null;
			$ctrl.shPostSelected = null;
			$ctrl.ok = function() {
				$uibModalInstance.close($ctrl.shPostSelected);
			};

			$ctrl.cancel = function() {
				$ctrl.shPostSelected = null;
				$uibModalInstance.dismiss('cancel');
			};
			$scope.shSite = null;
			$scope.shChannels = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;

			// BEGIN Functions
			$scope.channelList = function(channelId) {
				$ctrl.enableInsertButton = false;
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/channel/" + channelId + "/list/" + objectType))
						.then(function(response) {
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
								"/site/" + siteId + "/channel")).then(
						function(response) {
							$scope.shChannels = response.data.shChannels;
							$scope.shPosts = response.data.shPosts;
							$scope.shSite = response.data.shSite;
						}));
			}

			$scope.selectedPost = function(shPost) {
				$ctrl.shPostSelected = shPost;
				$ctrl.enableInsertButton = true;
			}
			
			$scope.selectedChannel = function(shChannel) {
				$ctrl.shChannelSelected = shChannel;
				$ctrl.enableInsertButton = true;
			}
			// END Functions

			$scope.channelList(channelId);

		} ]);