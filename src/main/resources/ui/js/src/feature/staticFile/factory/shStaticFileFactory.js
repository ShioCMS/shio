shioApp.factory('shStaticFileFactory', [
    '$q', 'Upload', 'shAPIServerService', '$timeout',
    function ($q, Upload, shAPIServerService) {
        return {
            uploadFile: function (folderId, shPostAttr, postType) {
                var deferredFile = $q.defer();
                if (shPostAttr.shPostTypeAttr.shWidget != null && shPostAttr.shPostTypeAttr.shWidget.name == "File" && shPostAttr.file != null) {
                    var createPost = true;
                    if (postType.name == "File") {
                        createPost = false;
                    }
                    file = shPostAttr.file;
                    if (!file.$error) {
                        Upload.upload({
                            url: shAPIServerService.get().concat('/v2/staticfile/upload'),
                            data: {
                                file: file,
                                folderId: folderId,
                                createPost: createPost
                            }
                        }).then(function (resp) {
                            if (createPost) {
                                shPostAttr.strValue = resp.data.id;
                                delete shPostAttr.referenceObject;
                            } else {
                                var filePath = resp.data.title;
                                shPostAttr.strValue = filePath;
                            }
                            deferredFile.resolve("Success");
                        }, null, function (evt) {
                            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        });
                    }
                } else {
                    deferredFile.resolve("Success");
                }
                return deferredFile.promise;
            }
        }
    }
]);