shioApp.controller('ShPostNewCtrl', [
    "$scope", "$http", "$window", "$stateParams", "$rootScope", "shAPIServerService", "shPostResource", "Notification", "$q", "shStaticFileFactory", "$filter",
    function ($scope, $http, $window, $stateParams, $rootScope, shAPIServerService, shPostResource, Notification, $q, shStaticFileFactory, $filter) {
        $scope.tinymceOptions = {
            plugins: 'shTinyMCE link image code lists paste advlist anchor autolink autoresize charmap codesample directionality emoticons hr insertdatetime legacyoutput media nonbreaking noneditable pagebreak preview print searchreplace tabfocus table template textpattern toc visualblocks visualchars wordcount',
            relative_urls: false,
            toolbar: ' undo redo | bold italic | alignleft aligncenter alignright | code | numlist bullist a11ycheck anchor charmap codesample ltr rtl | link image media shMailTo shAddImage shAddContent | format emoticons  insertdatetime nonbreaking pagebreak paste | preview print searchreplace | table tabledelete | tableprops tablerowprops tablecellprops | tableinsertrowbefore tableinsertrowafter tabledeleterow | tableinsertcolbefore tableinsertcolafter tabledeletecol | template toc visualblocks visualchars wordcount',
            menubar: 'edit insert tools view format table',
            min_height: 500
        };
        $scope.folderId = $stateParams.folderId;
        $scope.postTypeId = $stateParams.postTypeId;
        $scope.postTypeName = $stateParams.postTypeName;
        $scope.breadcrumb = null;
        $scope.shPost = null;
        $scope.shFolder = null;
        $scope.shSite = null;
        $scope.shPostType = null;
        $scope.tabs = [];
        $scope.allowPublish = false;

        $scope.$evalAsync($http.get(shAPIServerService.get().concat("/v2/folder/" + $scope.folderId + "/path")).then(function (response) {
            $scope.shFolder = response.data.currentFolder
            $rootScope.shFolder = $scope.shFolder;
            $scope.breadcrumb = response.data.breadcrumb;
            $scope.shSite = response.data.shSite;
        }));
        if ($scope.postTypeId != null) {
            $scope.$evalAsync($http.get(shAPIServerService.get().concat("/v2/post/type/" + $scope.postTypeId + "/post/model")).then(function (response) {
                $scope.shPost = response.data;
                $scope.shPostType = response.data.shPostType;
                if ($scope.shPostType.workflowPublishEntity != null && $scope.shPostType.workflowPublishEntity.trim().length > 0) {
                    $scope.allowPublish = false;
                }
                else {
                    $scope.allowPublish = true;
                }
                ///
                $scope.tabs = [];
                var tabName = $scope.shPost.shPostType.title;

                angular
                    .forEach($filter('orderBy')($scope.shPost.shPostAttrs, 'shPostTypeAttr.ordinal', false),
                        function (shPostAttr, key) {

                            if (shPostAttr.shPostTypeAttr.shWidget.name === 'Tab') {
                                tabName = shPostAttr.shPostTypeAttr.label;
                                var tab1 = {
                                    ordinal: shPostAttr.shPostTypeAttr.ordinal,
                                    name: tabName
                                }
                                $scope.tabs.push(tab1);

                            } else if (key === 0) {
                                var tab2 = {
                                    ordinal: 0,
                                    name: tabName
                                }
                                $scope.tabs.push(tab2);
                            }
                            shPostAttr["tab"] = tabName;
                        });
            }));
        } else {
            $scope.$evalAsync($http.get(shAPIServerService.get().concat("/v2/post/type/name/" + $scope.postTypeName + "/post/model")).then(function (response) {
                $scope.shPost = response.data;
                $scope.shPostType = response.data.shPostType;
            }));

        }
        $scope.postEditForm = "template/post/form.html";
        $scope.openPreviewURL = function () {
            var link = "/preview#!/" + $scope.shPost.id;
            $window.open(link, "_self");
        }

        var nestedUploadFile = function (promiseFiles, parentOfAttr, postType) {
            if (parentOfAttr.shPostAttrs != null) {
                angular
                    .forEach(parentOfAttr.shPostAttrs,
                        function (shPostAttrNested, key1) {
                            promiseFiles
                                .push(uploadFile(
                                    shPostAttrNested,
                                    postType));
                            nestedUploadFile(promiseFiles, shPostAttrNested, postType);
                        });
            } else if (parentOfAttr.shChildrenRelatorItems != null) {
                angular
                    .forEach(parentOfAttr.shChildrenRelatorItems,
                        function (shChildrenRelatorItem, key2) {
                            angular
                                .forEach(shChildrenRelatorItem.shChildrenPostAttrs,
                                    function (shChildrenPostAttrNested, key3) {
                                        promiseFiles
                                            .push(uploadFile(
                                                shChildrenPostAttrNested,
                                                postType));
                                        nestedUploadFile(promiseFiles, shChildrenPostAttrNested, postType);
                                    });
                        });
            }
        }

        var uploadFile = function (shPostAttr, postType) {
            var widgetSettingsObject = angular
                .fromJson(shPostAttr.shPostTypeAttr.widgetSettings);

            var shFolderId = $scope.folderId;
            if (widgetSettingsObject != null && widgetSettingsObject.file != null) {
                var folderIdSetting = widgetSettingsObject.file.folderId;
                if (folderIdSetting != null &&
                    folderIdSetting.trim().length > 0) {
                    shFolderId = folderIdSetting;
                }
            }
            return shStaticFileFactory.uploadFile(
                shFolderId, shPostAttr,
                postType);
        }
        $scope.postSave = function (publishStatus) {
            var promiseFiles = [];
            $scope.filePath = null;
            var postType = $scope.shPost.shPostType;
            nestedUploadFile(promiseFiles, $scope.shPost, postType);

            $q.all(promiseFiles).then(function (dataThatWasPassed) {
                $scope.shPost.publishStatus = publishStatus;
                if ($scope.shPost.id != null) {
                    $scope.shPost.$update(function () {
                        angular
                            .forEach($scope.shPost.shPostAttrs,
                                function (shPostAttr, key) {
                                    if (angular.equals(shPostAttr.shPostTypeAttr.shWidget.type, "JSON")) {
                                        shPostAttr.strValue = JSON.parse(shPostAttr.strValue);
                                    }
                                });
                        ///							
                        var tabName = $scope.shPost.shPostType.title;
                        angular
                            .forEach($filter('orderBy')($scope.shPost.shPostAttrs, 'shPostTypeAttr.ordinal', false),
                                function (shPostAttr, key) {

                                    if (shPostAttr.shPostTypeAttr.shWidget.name === 'Tab')
                                        tabName = shPostAttr.shPostTypeAttr.label;
                                    shPostAttr["tab"] = tabName;
                                });
                        ///  
                        Notification.warning('The ' + $scope.shPost.title + ' Post was update.');
                    });
                } else {
                    delete $scope.shPost.id;
                    $scope.shPost.shFolder = $scope.shFolder;
                    shPostResource.save($scope.shPost, function (response) {
                        $scope.shPost = response;
                        angular
                            .forEach($scope.shPost.shPostAttrs,
                                function (shPostAttr, key) {
                                    if (angular.equals(shPostAttr.shPostTypeAttr.shWidget.type, "JSON")) {
                                        shPostAttr.strValue = JSON.parse(shPostAttr.strValue);
                                    }
                                });
                        ///							
                        var tabName = $scope.shPost.shPostType.title;
                        angular
                            .forEach($filter('orderBy')($scope.shPost.shPostAttrs, 'shPostTypeAttr.ordinal', false),
                                function (shPostAttr, key) {

                                    if (shPostAttr.shPostTypeAttr.shWidget.name === 'Tab')
                                        tabName = shPostAttr.shPostTypeAttr.label;
                                    shPostAttr["tab"] = tabName;
                                });
                        ///  
                        Notification.warning('The ' + $scope.shPost.title + ' Post was created.');
                    });
                }
            });
        }
    }
]);