var shioharaApp = angular.module('shioharaApp', [ 'ui.router' ]);

shioharaApp.config(function($stateProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise('/content');
	$stateProvider.state('content', {
		url : '/content',
		templateUrl : 'template/content.html',
		controller : 'VecContentCtrl',
		data : {
			pageTitle : 'Content | Viglet Shiohara'
		}
	})
	.state('content.post-type-select', {
		url : '/post/type/select',
		templateUrl : 'template/post-type-select.html',
		controller : 'VecPostTypeSelectCtrl',
		data : {
			pageTitle : 'Post Type Select | Viglet Shiohara'
		}
	});

});

shioharaApp.controller('VecContentCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $state, $rootScope) {
			$scope.shUser = null;
			$rootScope.$state = $state;
			$scope.$evalAsync($http.get(jp_domain + "/api/user/1").then(
					function(response) {
						$scope.shUser = response.data;
					}));
		} ]);
shioharaApp.controller('VecSiteListCtrl', [
	"$scope",
	"$http",
	"$window",
	"$state",
	"$rootScope",
	function($scope, $http, $window, $state, $rootScope) {
		$scope.shSites = null;
		$rootScope.$state = $state;
		$scope.$evalAsync($http.get(jp_domain + "/api/site").then(
				function(response) {
					$scope.shSites = response.data;
				}));
	} ]);
shioharaApp.controller('VecPostTypeSelectCtrl', [
	"$scope",
	"$http",
	"$window",
	"$state",
	"$rootScope",
	function($scope, $http, $window, $state, $rootScope) {
		$scope.shPostTypes = null;
		$rootScope.$state = $state;
		$scope.$evalAsync($http.get(jp_domain + "/api/post/type").then(
				function(response) {
					$scope.shPostTypes = response.data;
				}));
	} ]);