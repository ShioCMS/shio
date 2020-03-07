shioApp.controller('ShWidgetFileSelectCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance',
		'shFolder',
		'shWidgetFileFactory',
		'Upload',
		'$timeout',
		function($scope, shAPIServerService, $http, $uibModalInstance,
				shFolder,shWidgetFileFactory, Upload, $timeout) {
			var $ctrl = this;

			$ctrl.shPostSelected = null;
			$ctrl.ok = function() {
				$uibModalInstance.close($ctrl.shPostSelected);
			};

			$ctrl.cancel = function() {
				$ctrl.shPostSelected = null;
				$uibModalInstance.dismiss('cancel');
			};
			$scope.shSite = null;
			$scope.shFolders = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;
			$scope.currentFolder = null;
			$ctrl.orderAsc = true;
			$ctrl.orderBy = 'name';
			$ctrl.orderByPostSelected = "title";
        	$ctrl.orderByFolderSelected = "name";
			$ctrl.shSearchFilter = "";
			
			// BEGIN Functions

			$ctrl.orderList = function () {
				if ($ctrl.orderAsc) {
					$ctrl.orderAsc = false;
				}
				else {
					$ctrl.orderAsc = true;
				}
				$ctrl.orderBySelection();
			}

			$ctrl.orderBySelection = function () {
				var orderPrefix = "";
				if (!$ctrl.orderAsc) {
					orderPrefix = "-";
				}
				
				if ($ctrl.orderBy === 'name') {
					$ctrl.orderByPostSelected = orderPrefix + "title";
					$ctrl.orderByFolderSelected = orderPrefix + "name";            
				}
				if ($ctrl.orderBy === 'date') {
					$ctrl.orderByPostSelected = orderPrefix + "date";
					$ctrl.orderByFolderSelected = orderPrefix + "date";
				}
			}

			$scope.showSearchFolderAction = function () {
				$scope.searchFolder = true;
				focus('search-folder-input');			
			}

			$scope.hideSearchFolderAction = function () {
				$scope.searchFolder = false;
				$ctrl.shSearchFilter = "";
			}

			$scope.folderList = function(shFolder) {
				$scope.currentFolder = shFolder;
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/object/" + shFolder.id + "/list/PT-FILE"))
						.then(function(response) {
							$scope.shFolders = response.data.shFolders;
							$scope.shPosts = response.data.shPosts;
							$scope.breadcrumb = response.data.breadcrumb;
							$scope.shSite = response.data.shSite;
						}));
			}

			$scope.siteList = function(siteId) {
				$ctrl.shPostSelected = null;
				$scope.$evalAsync($http.get(
						shAPIServerService.get().concat(
								"/v2/site/" + siteId + "/folder")).then(
						function(response) {
							$scope.shFolders = response.data.shFolders;
							$scope.shPosts = response.data.shPosts;
							$scope.shSite = response.data.shSite;
							$scope.breadcrumb = null;
						}));
			}

			$scope.selectedPost = function(shPost) {
				$ctrl.shPostSelected = shPost;
			}

			$scope.uploadFile = function(file, errFiles) {
				$scope.f = file;
				$scope.errFile = errFiles && errFiles[0];
				if (file) {
					file.upload = Upload.upload({
						url: shAPIServerService.get().concat('/v2/staticfile/upload'),
						data: {
							file: file,
							folderId:  $scope.currentFolder.id,
							createPost: true
						}
					});					
					file.upload.then(function (response) {
						$timeout(function () {						
							$scope.shPosts.unshift(response.data);
							file.result = response.data;
						});
					}, function (response) {
						if (response.status > 0)
							$scope.errorMsg = response.status + ': ' + response.data;
					}, function (evt) {
						file.progress = Math.min(100, parseInt(100.0 * 
												 evt.loaded / evt.total));
					});
				}   
			}			
			// END Functions

			$scope.folderList(shFolder);

		} ]);
