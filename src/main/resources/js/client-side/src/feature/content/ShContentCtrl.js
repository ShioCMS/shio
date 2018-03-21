shioharaApp.controller('ShContentCtrl', [ "$rootScope", "$scope","$state",
		'vigLocale', "$translate",
		function($rootScope, $scope, $state, vigLocale, $translate) {
			$scope.channelId = null;
			$scope.shUser = null;
			$scope.shSite = null;
			$scope.shPosts = null;
			$scope.shLastPostType = null;
			$scope.shChannels = null;
			$rootScope.$state = $state;
			$scope.breadcrumb = null;

		} ]);