shioharaApp.factory('shStaticFileFactory', [
	'$q', 'Upload', 'shAPIServerService', '$timeout'
		, function ($q, Upload, shAPIServerService, $timeout) {
        return {
            uploadFile: function (folderId, shPostAttr, key, postType, shPost, numberOfFileWidgets) {
                var deferredFile = $q.defer();
                if (shPostAttr.shPostTypeAttr.shWidget.name == "File" && shPostAttr.file != null) {
                    var createPost = true;
                    if (postType.name == "PT-FILE") {
                        createPost = false;
                    }
                    numberOfFileWidgets++;
                    file = shPostAttr.file;
                    if (!file.$error) {
                        Upload.upload({
                            url: shAPIServerService.get().concat('/v2/staticfile/upload')
                            , data: {
                                file: file
                                , folderId: folderId
                                , createPost: createPost
                            }
                        }).then(function (resp) {
                            filePath = resp.data.title;
                            if (createPost) {
                                shPost.shPostAttrs[key].strValue = resp.data.id;
                                delete shPost.shPostAttrs[key].referenceObjects;
                            }
                            else {
                                shPost.shPostAttrs[key].strValue = filePath;
                            }
                            deferredFile.resolve("Success");
                        }, null, function (evt) {
                            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        });
                    }
                }
                else {
                    deferredFile.resolve("Success");
                }
                return deferredFile.promise;
            }
        }
		}]);