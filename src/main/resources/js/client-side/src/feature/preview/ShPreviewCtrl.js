shPreviewApp.controller('ShPreviewCtrl', [ "$scope", "$state", "$stateParams","$sce",
		function($scope, $state, $stateParams, $sce) {
	$scope.trustSrc = function(src) {
	    return $sce.trustAsResourceUrl(src);
	  }
			$scope.objectId = $stateParams.objectId;
			$scope.previewURL = $sce.trustAsResourceUrl("http://localhost:2710/api/v2/object/" + $scope.objectId + "/preview");
			$scope.isMobile = false;
			console.log("Preview123:" + $scope.objectId);
		} ]);
