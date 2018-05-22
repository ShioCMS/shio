shioharaApp.controller('ShWidgetRelatorCtrl', [
		'$scope',
		'Upload',
		'$timeout',
		'$uibModal',
		'shWidgetRelatorFactory',
		'$filter',
		function($scope, Upload, $timeout, $uibModal, shWidgetRelatorFactory, $filter) {
			$scope.fileName = null;
			$scope.uploadNewFile = false;

			$scope.newFile = function() {
				$scope.uploadNewFile = true;
			}
			$scope.clearFile = function(shPostAttr) {
				shPostAttr.strValue = null;
				shPostAttr.file = null;
			}
			
			$scope.relatorDelete = function(shPostAttr,shChildrenRelatorItem) {
				  var foundItem = $filter('filter')(shPostAttr.shChildrenRelatorItems, {
                      id: shChildrenRelatorItem.id
                  }, true)[0];
                  // get the index
                  var index = shPostAttr.shChildrenRelatorItems.indexOf(foundItem);
                  // remove the item from array
                  shPostAttr.shChildrenRelatorItems.splice(index, 1);                  
			}
			
			$scope.addRelatorItem = function(shPost, shPostAttr) {
				var modalInstance = shWidgetRelatorFactory
						.modalAddRelatorItem(shPostAttr);
				modalInstance.result.then(function(shPostAttrs) {
					// Selected SAVE
					if ($scope.shPostAttr.shChildrenRelatorItems == null) {
						$scope.shPostAttr.shChildrenRelatorItems = [];

					}
					
					var shChilRelatorItem = {};
					shChilRelatorItem.id = null;
					shChilRelatorItem.shChildrenPostAttrs = shPostAttrs;
					
					$scope.shPostAttr.shChildrenRelatorItems.push(shChilRelatorItem);

				}, function() {
					// Selected CANCEL
				});
			}

			$scope.selectRelatorItem = function(shPost, shChildrenRelatorItem) {
				var modalInstance = shWidgetRelatorFactory
						.modalSelectRelatorItem(shChildrenRelatorItem);
				modalInstance.result.then(function(shPostSelected) {
					// Selected SAVE
				}, function() {
					// Selected CANCEL
				});
			}
		} ]);
