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
	}).state('content.post-type-select', {
		url : '/post/type/select',
		templateUrl : 'template/post/type/select.html',
		controller : 'VecPostTypeSelectCtrl',
		data : {
			pageTitle : 'Post Type Select | Viglet Shiohara'
		}
	}).state('content.post-type-editor', {
		url : '/post/type/select',
		templateUrl : 'template/post/type/editor.html',
		controller : 'VecPostTypeSelectCtrl',
		data : {
			pageTitle : 'Post Type Editor | Viglet Shiohara'
		}
	}).state('content.post-type-attributes', {
		url : '/post/type/:postTypeId/attributes',
		templateUrl : 'template/post/type/attributes.html',
		controller : 'VecPostTypeAttrsCtrl',
		data : {
			pageTitle : 'Post Type Editor | Viglet Shiohara'
		}
	}).state('content.post-type-attribute', {
		url : '/post/type/attributes/:postTypeAttrId',
		templateUrl : 'template/post/type/attribute.html',
		controller : 'VecPostTypeAttrCtrl',
		data : {
			pageTitle : 'Post Type Editor | Viglet Shiohara'
		}
	});
	;

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
			$scope.$evalAsync($http.get(jp_domain + "/api/user/2").then(
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

shioharaApp.controller('VecPostTypeAttrsCtrl', [
		"$scope",
		"$http",
		"$window",
		"$stateParams",
		"$state",
		"$rootScope",
		function($scope, $http, $window, $stateParams, $state, $rootScope) {
			$scope.postTypeId = $stateParams.postTypeId;
			$scope.shPostType = null;
			$rootScope.$state = $state;
			$scope.$evalAsync($http.get(
					jp_domain + "/api/post/type/" + $scope.postTypeId).then(
					function(response) {
						$scope.shPostType = response.data;
					}));

			$scope.postTypeSave = function() {
				var parameter = angular.toJson($scope.shPostType);
				$http.put(jp_domain + "/api/post/type/" + $scope.postTypeId,
						parameter).then(
						function(data, status, headers, config) {
							$state.go('content.post-type-attributes');
						}, function(data, status, headers, config) {
							$state.go('content.post-type-attributes');
						});
			}
		} ]);

shioharaApp.controller('VecPostTypeAttrCtrl', [
	"$scope",
	"$http",
	"$window",
	"$stateParams",
	"$state",
	"$rootScope",
	function($scope, $http, $window, $stateParams, $state, $rootScope) {	
		$scope.postTypeAttrId = $stateParams.postTypeAttrId;
		$scope.shPostTypeAttr = null;
		$rootScope.$state = $state;
		$scope.$evalAsync($http.get(
				jp_domain + "/api/post/type/attr/" + $scope.postTypeAttrId).then(
				function(response) {
					$scope.shPostTypeAttr = response.data;
				}));

	} ]);