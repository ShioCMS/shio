shioApp.controller('ShObjectChildrenCtrl', [
    "$scope"
    , "$state"
    , "$stateParams"
    , "$rootScope"
    , "$translate"
    , "$http"
    , "$window"
    , "shAPIServerService"
    , 'vigLocale'
    , "shFolderFactory"
    , "shPostFactory"
    , "ShDialogSelectObject"
    , "ShDialogImportFromProvider"
    , "ShDialogDeleteFactory"
    , "shPostResource"
    , "shFolderResource"
    , "$filter"
    , "Notification"
    , "moment"
    , "shUserResource"
    , "shPostTypeResource"
    , "shSiteFactory"
    , "focus"
    , "shStaticFileUploadFactory"
    , function ($scope, $state, $stateParams, $rootScope, $translate, $http, $window, shAPIServerService, vigLocale, shFolderFactory, shPostFactory, ShDialogSelectObject, ShDialogImportFromProvider, ShDialogDeleteFactory, shPostResource, shFolderResource, $filter, Notification, moment, shUserResource, shPostTypeResource, shSiteFactory, focus, shStaticFileUploadFactory) {
        $scope.loadedObjectList = false;
        $scope.objectId = $stateParams.objectId;
        $scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
        $translate.use($scope.vigLanguage);
        $scope.shCurrentFolder = null;
        $scope.shSite = null;
        $scope.shFolders = null;
        $scope.shPosts = null;
        $scope.breadcrumb = null;
        $rootScope.$state = $state;
        $scope.shStateObjects = [];
        $scope.shObjects = [];
        $scope.actions = [];
        $scope.shUser = null;
        $scope.shLastPostType = null;
        $scope.itemSelected = false;
        $scope.searchFolder = false;
        $scope.shSearchFilter = "";
        $scope.orderAsc = true;
        $scope.orderBy = 'order';
        $scope.orderByPostSelected = null;
        $scope.orderByFolderSelected = null;
        $scope.sortableEnabled = true;


        if ($rootScope.principal == null) {
            $http.get(shAPIServerService.get().concat("/v2/user/current"))
                .then(function (response) {
                    if (response.data.username) {
                        $rootScope.principal = response.data;
                    }
                });
        }

        $scope.publishStatus = function (shPost) {
            if (!shPost.published) {
                return "Unpublished";
            }
            else {
                if (shPost.publishStatus === 'DRAFT') {
                    return "Stale";
                }
                else {
                    return "Published";
                }
            }
        }
        $scope.uploadFiles = function (folderId) {
            var modalInstance = shStaticFileUploadFactory.modalUploadFiles(folderId);
            modalInstance.result.then(function () {
                $scope.loadObjects();
            }, function () {
                // Selected CANCEL
            });
        }
        $scope.checkSomeItemSelected = function () {
            $scope.itemSelected = false;
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shStateObjects[stateKey]) {
                    $scope.itemSelected = true;
                }
            }
        }

        $scope.showSearchFolderAction = function () {
            $scope.searchFolder = true;
            focus('search-folder-input');
            $scope.sortableFolders.disabled = true;
            $scope.sortablePosts.disabled = true;
        }
        $scope.hideSearchFolderAction = function () {
            $scope.searchFolder = false;
            $scope.shSearchFilter = "";
            $scope.sortableFolders.disabled = false;
            $scope.sortablePosts.disabled = false;
        }
        $scope.siteExport = function () {
            shSiteFactory.export($scope.shSite);
        }

        $scope.orderList = function () {
            if ($scope.orderAsc) {
                $scope.orderAsc = false;
            }
            else {
                $scope.orderAsc = true;
            }
            $scope.orderBySelection();
        }
        $scope.orderBySelection = function () {
            var orderPrefix = "";
            if (!$scope.orderAsc) {
                orderPrefix = "-";
            }
            if ($scope.orderBy === 'order') {
                $scope.orderByPostSelected = null;
                $scope.orderByFolderSelected = null;
                $scope.sortableFolders.disabled = false;
                $scope.sortablePosts.disabled = false;
            }
            if ($scope.orderBy === 'name') {
                $scope.orderByPostSelected = orderPrefix + "title";
                $scope.orderByFolderSelected = orderPrefix + "name";
                $scope.sortableFolders.disabled = true;
                $scope.sortablePosts.disabled = true;
            }
            if ($scope.orderBy === 'description') {
                $scope.orderByPostSelected = orderPrefix + "summary";
                $scope.orderByFolderSelected = orderPrefix + "summary";
                $scope.sortableFolders.disabled = true;
                $scope.sortablePosts.disabled = true;
            }
            if ($scope.orderBy === 'date') {
                $scope.orderByPostSelected = orderPrefix + "date";
                $scope.orderByFolderSelected = orderPrefix + "date";
                $scope.sortableFolders.disabled = true;
                $scope.sortablePosts.disabled = true;
            }
        }
        $scope.sortableFolders = {
            disabled: false,
            stop: function (e, ui) {
                var sortObject = {};               
                angular.forEach($scope.shFolders, function (shFolder, key) {
                    sortObject[shFolder.id] = shFolder.position;
                });
                var parameter = JSON.stringify(sortObject);
                $http.put(shAPIServerService.get().concat("/v2/object/sort"), parameter).then(function (response) { });
            }
        };
        $scope.sortablePosts = {
            disabled: false,
            stop: function (e, ui) {
                var sortObject = {};
                angular.forEach($scope.shPosts, function (shPost, key) {
                    sortObject[shPost.id] = shPost.position;
                });
                var parameter = JSON.stringify(sortObject);
                $http.put(shAPIServerService.get().concat("/v2/object/sort"), parameter).then(function (response) { });
            }
        };
        $scope.shUser = shUserResource.get({
            id: "admin"
            , access_token: $scope.accessToken
        }, function () {
            $scope.shLastPostType = shPostTypeResource.get({
                id: $scope.shUser.lastPostType
            });
        });
        $scope.$evalAsync($http.get(shAPIServerService.get().concat("/v2/history/object/" + $scope.objectId + "/count")).then(function (response) {
            $scope.commits = response.data;
        }));
        $scope.loadObjects = function () {
            $scope.$evalAsync($http.get(shAPIServerService.get().concat("/v2/object/" + $scope.objectId + "/list")).then(function (response) {
                $scope.processResponse(response);
                $scope.loadedObjectList = true;
            }));
        }

        $scope.processResponse = function (response) {
            $scope.shFolders = response.data.shFolders;
            $scope.shFolders = $filter('orderBy')($scope.shFolders, 'position');
            $scope.shPosts = response.data.shPosts;
            $scope.shPosts = $filter('orderBy')($scope.shPosts, 'position');
            $scope.breadcrumb = response.data.breadcrumb;
            $scope.shCurrentFolder = null;
            if ($scope.breadcrumb != null) {
                $scope.shCurrentFolder = $scope.breadcrumb.slice(-1).pop();
            }
            $scope.shSite = response.data.shSite;
            angular.forEach($scope.shFolders, function (shFolder, key) {
                $scope.shStateObjects[shFolder.id] = false;
                $scope.shObjects[shFolder.id] = shFolder;
                $scope.actions[shFolder.id] = false;
            });
            angular.forEach($scope.shPosts, function (shPost, key) {
                $scope.shStateObjects[shPost.id] = false;
                $scope.shObjects[shPost.id] = shPost;
                $scope.actions[shPost.id] = false;
            });
            $scope.itemSelected = false;
        }
        $scope.selectContents = function () {
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shObjects[stateKey].objectType === "POST") {
                    $scope.shStateObjects[stateKey] = true;
                    $scope.itemSelected = true;
                }
                else {
                    $scope.shStateObjects[stateKey] = false;
                }
            }
            $scope.checkSomeItemSelected();
        }
        $scope.selectFolders = function () {
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shObjects[stateKey].objectType === "FOLDER") {
                    $scope.shStateObjects[stateKey] = true;
                }
                else {
                    $scope.shStateObjects[stateKey] = false;
                }
            }
            $scope.checkSomeItemSelected();
        }
        $scope.selectEverything = function () {
            for (var stateKey in $scope.shStateObjects) {
                $scope.shStateObjects[stateKey] = true;
            }
            $scope.checkSomeItemSelected();
        }
        $scope.selectNothing = function () {
            for (var stateKey in $scope.shStateObjects) {
                $scope.shStateObjects[stateKey] = false;
            }
            $scope.itemSelected = false;
        }
        $scope.selectInverted = function () {
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shStateObjects[stateKey]) {
                    $scope.shStateObjects[stateKey] = false;
                }
                else {
                    $scope.shStateObjects[stateKey] = true;
                }
            }
            $scope.checkSomeItemSelected();
        }
        $scope.updateAction = function (id, value) {
            $scope.actions[id] = value;
            $scope.checkSomeItemSelected();
        }
        $scope.isRecent = function (date) {
            var momentDate = new moment(date);
            var now = new moment();
            var duration = moment.duration(momentDate.diff(now))
            if (duration.as('minutes') >= -5) {
                return true;
            }

            return false;
        }
        $scope.objectsCopy = function () {
            var objectGlobalIds = [];
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shStateObjects[stateKey] === true) {
                    var objectGlobalId = "" + stateKey;
                    objectGlobalIds.push(objectGlobalId);
                }
            }
            $scope.objectsCopyDialog(objectGlobalIds);
        }
        $scope.objectCopy = function (shObject) {
            var objectGlobalIds = [];
            objectGlobalIds.push(shObject.id);
            $scope.objectsCopyDialog(objectGlobalIds);
        }

        $scope.importFromProvider = function (folderId) {
            var modalInstance = ShDialogImportFromProvider.dialog(2000, "shFolder");
            modalInstance.result.then(function (providerItem) {
                console.log(providerItem);
                $http.post(shAPIServerService.get().concat("/v2/provider/exchange/otds/import/" + providerItem.id + "/to/" + folderId)).then(function (response) {
                    var shPost = response.data;
                    $scope.shPosts.push(shPost);
                    Notification.warning(shPost.title + " Asset was imported.");
                });
            }, function () {
                // Selected CANCEL
            });
        }

        $scope.objectsCopyDialog = function (objectGlobalIds) {
            var modalInstance = ShDialogSelectObject.dialog($scope.objectId, "shFolder");
            modalInstance.result.then(function (shObjectSelected) {
                var parameter = JSON.stringify(objectGlobalIds);
                $http.put(shAPIServerService.get().concat("/v2/object/copyto/" + shObjectSelected.id), parameter).then(function (response) {
                    var shObjects = response.data;
                    for (var i = 0; i < shObjects.length; i++) {
                        var shObject = shObjects[i];
                        if (shObjectSelected.id == $scope.objectId) {
                            $scope.shStateObjects[shObject.id] = false;
                            $scope.shObjects[shObject.id] = shObject;
                            $scope.actions[shObject.id] = false;
                        }
                        var copiedMessage = null;
                        if (shObject.objectType == "POST") {
                            if (shObjectSelected.id == $scope.objectId) {
                                $scope.shPosts.push(shObject);
                            }
                            copiedMessage = 'The ' + shObject.title + ' Post was copied.';
                        }
                        else if (shObject.objectType == "FOLDER") {
                            if (shObjectSelected.id == $scope.objectId) {
                                $scope.shFolders.push(shObject);
                            }
                            copiedMessage = 'The ' + shObject.name + ' Folder was copied.';
                        }
                        Notification.warning(copiedMessage);
                    }
                    $scope.checkSomeItemSelected();
                });
            }, function () {
                // Selected CANCEL
            });
        }
        $scope.objectsMove = function () {
            var objectGlobalIds = [];
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shStateObjects[stateKey] === true) {
                    var objectGlobalId = "" + stateKey;
                    objectGlobalIds.push(objectGlobalId);
                }
            }
            $scope.objectsMoveDialog(objectGlobalIds);
        }
        $scope.objectMove = function (shObject) {
            var objectGlobalIds = [];
            objectGlobalIds.push(shObject.id);
            $scope.objectsMoveDialog(objectGlobalIds);
        }
        $scope.objectsMoveDialog = function (objectGlobalIds) {
            var modalInstance = ShDialogSelectObject.dialog($scope.objectId, "shFolder");
            modalInstance.result.then(function (shObjectSelected) {
                if (shObjectSelected.id == $scope.objectId) {
                    var movedMessageSameFolder = 'No moved, because you selected the same folder as destination';
                    Notification.warning(movedMessageSameFolder);
                }
                else {
                    var parameter = JSON.stringify(objectGlobalIds);
                    $http.put(shAPIServerService.get().concat("/v2/object/moveto/" + shObjectSelected.id), parameter).then(function (response) {
                        var shObjects = response.data;
                        for (var i = 0; i < shObjects.length; i++) {
                            var shObject = shObjects[i];
                            $scope.shStateObjects[shObject.id] = false;
                            var movedMessagePost = null;
                            if (shObject.objectType == "POST") {
                                movedMessagePost = 'The ' + shObject.title + ' Post was moved.';
                                var foundItem = $filter('filter')
                                    ($scope.shPosts, {
                                        id: shObject.id
                                    }, true)[0];
                                var index = $scope.shPosts.indexOf(foundItem);
                                $scope.shPosts.splice(index, 1);
                            }
                            else if (shObject.objectType == "FOLDER") {
                                movedMessageFolder = 'The ' + shObject.name + ' Folder was moved.';
                                var foundItemFolder = $filter('filter')
                                    ($scope.shFolders, {
                                        id: shObject.id
                                    }, true)[0];
                                var indexFolder = $scope.shFolders.indexOf(foundItemFolder);
                                $scope.shFolders.splice(indexFolder, 1);
                            }
                            Notification.warning(movedMessageFolder);
                        }
                        $scope.checkSomeItemSelected();
                    });
                }
            }, function () {
                // Selected CANCEL
            });
        }
        $scope.objectClone = function (shObject) {
            var objectGlobalIds = [];
            objectGlobalIds.push(shObject.id);
            var parameter = JSON.stringify(objectGlobalIds);
            var parentObjectId = null;
            if ($scope.shCurrentFolder == null) {
                parentObjectId = $scope.shSite.id;
            }
            else {
                parentObjectId = $scope.shCurrentFolder.id;
            }
            $http.put(shAPIServerService.get().concat("/v2/object/copyto/" + parentObjectId), parameter).then(function (response) {
                var shObjects = response.data;
                for (var i = 0; i < shObjects.length; i++) {
                    shObject = shObjects[i];
                    $scope.shStateObjects[shObject.id] = false;
                    $scope.shObjects[shObject.id] = shObject;
                    $scope.actions[shObject.id] = false;
                    var clonedMessage = null;
                    if (shObject.objectType == "POST") {
                        $scope.shPosts.unshift(shObject);
                        clonedMessage = 'The ' + shObject.title + ' Post was cloned.';
                    }
                    else if (shObject.objectType == "FOLDER") {
                        $scope.shFolders.unshift(shObject);
                        clonedMessage = 'The ' + shObject.name + ' Folder was cloned.';
                    }
                    Notification.warning(clonedMessage);
                }
                $scope.checkSomeItemSelected();
            });
        }

        $scope.objectClearCache = function (shObjectItem) {
            $http.get(shAPIServerService.get().concat("/v2/object/" + shObjectItem.id + "/clear-cache")).then(function (response) {
                var shObject = response.data;
                var clearMessage = null;
                if (shObject.objectType == "POST") {
                    clearMessage = 'The ' + shObject.title + ' Post Cache has been cleared.';
                }
                else if (shObject.objectType == "FOLDER") {
                    clearMessage = 'The ' + shObject.name + ' Folder Cache has been cleared.';
                }
                Notification.warning(clearMessage);
            });
        }

        $scope.objectsRename = function () {
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shStateObjects[stateKey] === true) {
                    console.log("Rename " + stateKey);
                    $scope.shStateObjects[stateKey] = false;
                }
            }
            $scope.checkSomeItemSelected();
        }
        $scope.objectsDelete = function () {
            var shSelectedObjects = [];
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shStateObjects[stateKey] === true) {
                    shSelectedObjects.push($scope.shObjects[stateKey]);
                }
            }
            $scope.objectsDeleteDialog(shSelectedObjects);
        }

        $scope.folderDelete = function (shFolder) {
            shFolderFactory.deleteFromList(shFolder, $scope.shFolders);
        }
        $scope.postDelete = function (shPost) {
            shPostFactory.deleteFromList(shPost, $scope.shPosts);
        }
        $scope.objectsDeleteDialog = function (shSelectedObjects) {
            var modalInstance = ShDialogDeleteFactory.dialog(shSelectedObjects);
            modalInstance.result.then(function () {
                angular.forEach(shSelectedObjects, function (value, key) {
                    $scope.shStateObjects[value.id] = false;
                    if (value.objectType === "POST") {
                        var shPost = value;
                        var deletedMessage = 'The ' + shPost.title + ' Post was deleted.';
                        shPostResource.delete({
                            id: shPost.id
                        }, function () {
                            delete $scope.shStateObjects[shPost.id];
                            // filter the array
                            var foundItem = $filter('filter')($scope.shPosts, {
                                id: shPost.id
                            }, true)[0];
                            // get the index
                            var index = $scope.shPosts.indexOf(foundItem);
                            // remove the item from array
                            $scope.shPosts.splice(index, 1);
                            Notification.error(deletedMessage);
                        });
                    }
                    else if (value.objectType === "FOLDER") {
                        var shFolder = value;
                        var deletedMessageFolder = 'The ' + shFolder.name + ' Folder was deleted.';
                        shFolderResource.delete({
                            id: shFolder.id
                        }, function () {
                            delete $scope.shStateObjects[shFolder.id];
                            // filter the array
                            var foundItemFolder = $filter('filter')($scope.shFolders, {
                                id: shFolder.id
                            }, true)[0];
                            // get the index
                            var indexFolder = $scope.shFolders.indexOf(foundItemFolder);
                            // remove the item from array
                            $scope.shFolders.splice(indexFolder, 1);
                            Notification.error(deletedMessageFolder);
                        });
                    }
                    $scope.checkSomeItemSelected();
                });
            }, function () {
                // Selected CANCEL
            });
        }
        $scope.objectPreview = function (shObject) {
            //var link = shAPIServerService.get().concat("/v2/object/" + shObject.id + "/preview");
            var link = "/preview#!/" + shObject.id;
            $window.open(link, "_self");
        }
        $scope.loadObjects();
    }]);