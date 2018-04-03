shioharaApp.controller('ShPostNewCtrl', [
						"$scope"
						, "$http"
						, "$window"
						, "$stateParams"
						, "$state"
						, "$rootScope"
						, "shAPIServerService"
						, "shPostResource"
						, "Notification"
						, "Upload"
						, "$q"
						, "$timeout"
						, "shStaticFileFactory"
						, function ($scope, $http, $window, $stateParams, $state, $rootScope, shAPIServerService, shPostResource, Notification, Upload, $q, $timeout, shStaticFileFactory) {
        $scope.tinymceOptions = {
            plugins: 'link image code'
            , toolbar: 'undo redo | bold italic | alignleft aligncenter alignright | code'
        };
        $scope.folderId = $stateParams.folderId;
        $scope.postTypeId = $stateParams.postTypeId;
        $scope.breadcrumb = null;
        $scope.shPost = null;
        $scope.shFolder = null;
        $scope.shSite = null;
        var folderURL = null;
        $scope.$evalAsync($http.get(shAPIServerService.get().concat("/v2/folder/" + $scope.folderId + "/path")).then(function (response) {
            $scope.shFolder = response.data.currentFolder
            $scope.breadcrumb = response.data.breadcrumb;
            $scope.shSite = response.data.shSite;
            folderPath = shAPIServerService.server().concat("/v1/store/file_source/" + $scope.shSite.name + response.data.folderPath);
            folderURL = shAPIServerService.server().concat("/v2/sites/" + $scope.shSite.name.replace(new RegExp(" ", 'g'), "-") + "/default/pt-br" + response.data.folderPath.replace(new RegExp(" ", 'g'), "-"));
        }));
        $scope.$evalAsync($http.get(shAPIServerService.get().concat("/v2/post/type/" + $scope.postTypeId + "/post/model")).then(function (response) {
            $scope.shPost = response.data;
        }));
        $scope.postEditForm = "template/post/form.html";
        $scope.openPreviewURL = function () {
			 var link = shAPIServerService.get().concat("/v2/object/" + $scope.shPost.shGlobalId.id + "/preview");
	         $window.open(link,"_self");

        }
        var uploadFile = function (shPostAttr, key, postType) {
            return shStaticFileFactory.uploadFile($scope.folderId, shPostAttr, key, postType, $scope.shPost, $scope.numberOfFileWidgets);
        }
        $scope.postSave = function () {
            var promiseFiles = [];
            $scope.filePath = null;
            $scope.numberOfFileWidgets = 0;
            var postType = $scope.shPost.shPostType;
            angular.forEach($scope.shPost.shPostAttrs, function (shPostAttr, key) {
                promiseFiles.push(uploadFile(shPostAttr, key, postType));
            });
            $q.all(promiseFiles).then(function (dataThatWasPassed) {
                if ($scope.shPost.id != null) {
                    $scope.shPost.$update(function () {
                        Notification.warning('The ' + $scope.shPost.title + ' Post was update.');
                        // $state.go('content');
                    });
                }
                else {
                    delete $scope.shPost.id;
                    $scope.shPost.shFolder = $scope.shFolder;
                    shPostResource.save($scope.shPost, function (response) {
                        $scope.shPost = response;
                        Notification.warning('The ' + $scope.shPost.title + ' Post was created.');
                    });
                }
            });
        }
						}]);