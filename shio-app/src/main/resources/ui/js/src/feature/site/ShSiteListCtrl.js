shioApp.controller('ShSiteListCtrl', [
	"$scope",
	"$http",
	"$window",
	"$state",
	"$rootScope",
	"shSiteResource",
	function ($scope, $http, $window, $state, $rootScope, shSiteResource) {
		$rootScope.$state = $state;
		$scope.shStateObjects = [];
		$scope.shObjects = [];
		$scope.actions = [];
		$scope.itemSelected = false;
		$scope.shSites = shSiteResource.query(function () {
			angular.forEach($scope.shSites, function (shSite, key) {
				$scope.shStateObjects[shSite.id] = false;
				$scope.shObjects[shSite.id] = shSite;
				$scope.actions[shSite.id] = false;
			});
		});

		$scope.objectPreview = function (shObject) {
			var link = "/preview#!/" + shObject.id;
			$window.open(link, "_self");
		}

		$scope.updateAction = function (id, value) {
			$scope.actions[id] = value;
			$scope.checkSomeItemSelected();
		}

		$scope.checkSomeItemSelected = function () {
			$scope.itemSelected = false;
			for (var stateKey in $scope.shStateObjects) {
				if ($scope.shStateObjects[stateKey]) {
					$scope.itemSelected = true;
				}
			}
		}

		$scope.isRecent = function (date) {
            var momentDate = moment(date);
            var now = new moment();
            var duration = moment.duration(momentDate.diff(now))
            if (duration.as('minutes') >= -5) {
                return true;
            }

            return false;
        }

	}]);
