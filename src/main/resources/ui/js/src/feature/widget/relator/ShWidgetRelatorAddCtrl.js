shioharaApp.controller('ShWidgetRelatorAddCtrl', [
	'$scope',
	'shAPIServerService',
	'$http',
	'$uibModalInstance',
	'shPostAttr',
	'orderByFilter',
	function ($scope, shAPIServerService, $http, $uibModalInstance,
		shPostAttr, orderByFilter) {

		$scope.tinymceOptions = {
			plugins: 'shTinyMCE link image code lists paste advlist anchor autolink autoresize charmap codesample directionality emoticons hr insertdatetime legacyoutput media nonbreaking noneditable pagebreak preview print searchreplace tabfocus table template textpattern toc visualblocks visualchars wordcount',
			relative_urls: false,
			toolbar: ' undo redo | bold italic | alignleft aligncenter alignright | code | numlist bullist a11ycheck anchor charmap codesample ltr rtl | link image media shMailTo shAddImage shAddContent | format emoticons  insertdatetime nonbreaking pagebreak paste | preview print searchreplace | table tabledelete | tableprops tablerowprops tablecellprops | tableinsertrowbefore tableinsertrowafter tabledeleterow | tableinsertcolbefore tableinsertcolafter tabledeletecol | template toc visualblocks visualchars wordcount',
			menubar: 'edit insert tools view format table',
			min_height: 500
		};

		$scope.shPostAttrs = [];
		$scope.$evalAsync($http.get(
			shAPIServerService.get().concat("/v2/post/attr/model"))
			.then(function (response) {
				$scope.shPostAttrModel = response.data;

				$scope.shPostTypeAttrs = shPostAttr.shPostTypeAttr.shPostTypeAttrs;

				angular.forEach($scope.shPostTypeAttrs, function (value, key) {
					var attrModel = angular.copy($scope.shPostAttrModel);
					attrModel.shPostTypeAttr = value;
					this.push(attrModel);
				}, $scope.shPostAttrs);
			}));

		var $ctrl = this;

		$ctrl.shPostSelected = null;

		$scope.shSite = null;
		$scope.shFolders = null;
		$scope.shPosts = null;
		$scope.breadcrumb = null;

		// BEGIN Functions
		$ctrl.ok = function () {
			$scope.titles = [];
			$scope.summaries = [];
			angular.forEach(orderByFilter($scope.shPostAttrs, 'shPostTypeAttr.ordinal', false), function (shPostAttr, key) {
				if (shPostAttr.shPostTypeAttr.isTitle == 1) {
					if (shPostAttr.dateValue !== null) {
						$scope.titles.push($ctrl.formatDate(shPostAttr.dateValue));
					}
					else {
						$scope.titles.push(shPostAttr.referenceObject != null ? shPostAttr.referenceObject.title : shPostAttr.strValue);
					}
				}
				if (shPostAttr.shPostTypeAttr.isSummary == 1) {
					if (shPostAttr.dateValue !== null) {
						$scope.summaries.push($ctrl.formatDate(shPostAttr.dateValue));
					}
					else {
						$scope.summaries.push(shPostAttr.referenceObject != null ? shPostAttr.referenceObject.summary : shPostAttr.strValue);
					}
				}

			});
			$scope.title = $scope.titles.join(", ");
			$scope.summary = $scope.summaries.join(", ");
			$uibModalInstance.close({
				shPostAttrs: $scope.shPostAttrs,
				title: $scope.title,
				summary: $scope.summary
			});
		};

		$ctrl.cancel = function () {
			$ctrl.shPostSelected = null;
			$uibModalInstance.dismiss('cancel');
		};
		// END Functions

		$ctrl.formatDate = function (date) {
			var d = new Date(date),
				month = '' + (d.getMonth() + 1),
				day = '' + d.getDate(),
				year = d.getFullYear();

			if (month.length < 2) month = '0' + month;
			if (day.length < 2) day = '0' + day;

			return [day, month, year].join('/');
		}
	}
]);