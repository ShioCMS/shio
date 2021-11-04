shioApp.controller('ShPostTypeSelectCtrl', [
	"$scope",
	"$state",
	"$rootScope",
	"$filter",
	"$http",
	"shPostTypeResource",
	"shAPIServerService",
	"Notification",
	function ($scope, $state, $rootScope, $filter, $http, shPostTypeResource, shAPIServerService, Notification) {
		$rootScope.$state = $state;
		$scope.shPostTypes = shPostTypeResource.query(function () {
			angular.forEach($scope.shPostTypes, function (shPostType, key) {
				$scope.shStateObjects[shPostType.id] = false;
				$scope.shObjects[shPostType.id] = shPostType;
				$scope.actions[shPostType.id] = false;
			});
		});
		$scope.shSite = null;
		$scope.breadcrumb = null;
		$scope.shStateObjects = [];
		$scope.shObjects = [];
		$scope.actions = [];
		$scope.itemSelected = false;

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

		$scope.removePostType = function (shPostType) {
			shPostTypeResource
				.delete({
					id: shPostType.id
				}, function () {
					delete $scope.shStateObjects[shPostType.id];
					// filter the array
					var foundItem = $filter('filter')($scope.shPostTypes, {
						id: shPostType.id
					}, true)[0];
					// get the index
					var index = $scope.shPostTypes.indexOf(foundItem);
					// remove the item from array
					$scope.shPostTypes.splice(index, 1);
					Notification.error('The ' + shPostType.name + ' Post Type was deleted.');
				});
		}

		$scope.postTypeClone = function (shPostType) {
			var postTypeIds = [];
			postTypeIds.push(shPostType.id);
			var parameter = JSON.stringify(postTypeIds);
			$http.put(shAPIServerService.get().concat("/v2/post/type/clone/"), parameter).then(function (response) {
				var shPostTypes = response.data;
				for (var i = 0; i < shPostTypes.length; i++) {
					var shPostTypeNew = shPostTypes[i];
					$scope.shStateObjects[shPostTypeNew.id] = false;
					$scope.shObjects[shPostTypeNew.id] = shPostTypeNew;
					$scope.actions[shPostTypeNew.id] = false;
					$scope.shPostTypes.unshift(shPostTypeNew);

					Notification.warning('The ' + shPostType.title + ' Post Type was cloned.');
				}
				$scope.checkSomeItemSelected();
			});
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
