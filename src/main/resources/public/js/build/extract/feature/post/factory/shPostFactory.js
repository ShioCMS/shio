shioharaApp.factory('shPostFactory', [
	'$uibModal', 'shPostResource', 'Notification', '$filter', "$state"



    
    , function ($uibModal, shPostResource, Notification, $filter, $state) {
        return {
            deleteFromList: function (shPost, shPosts) {
                var modalInstance = this.modalDelete(shPost);
                modalInstance.result.then(function (removeInstance) {
                    var deletedMessage = 'The ' + shPost.title + ' Post was deleted.';
                    shPostResource.delete({
                        id: shPost.id
                    }, function () {
                        // filter the array
                        var foundItem = $filter('filter')(shPosts, {
                            id: shPost.id
                        }, true)[0];
                        // get the index
                        var index = shPosts.indexOf(foundItem);
                        // remove the item from array
                        shPosts.splice(index, 1);
                        Notification.error(deletedMessage);
                    });
                }, function () {
                    // Selected NO
                });
            }
            , delete: function (shPost) {
                var modalInstance = this.modalDelete(shPost);
                modalInstance.result.then(function (removeInstance) {
                    deletedMessage = 'The ' + shPost.title + ' Post was deleted.';
                    shPostResource.delete({
                        id: shPost.id
                    }, function () {
                        Notification.error(deletedMessage);
                        $state.go('content.children.channel-children', {
                            channelId: shPost.shChannel.id
                        });
                    });
                }, function () {
                    // Selected NO
                });
            }
            , modalDelete: function (shPost) {
                var $ctrl = this;
                return $uibModal.open({
                    animation: true
                    , ariaLabelledBy: 'modal-title'
                    , ariaDescribedBy: 'modal-body'
                    , templateUrl: 'template/modal/shDeleteObject.html'
                    , controller: 'ShModalDeleteObjectCtrl'
                    , controllerAs: '$ctrl'
                    , size: null
                    , appendTo: undefined
                    , resolve: {
                        shPost: function () {
                            return shPost;
                        }
                    }
                });
            }
        }
		}]);