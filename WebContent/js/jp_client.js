var japodeApp = angular.module('japodeApp', ['angularMoment', 'ngSanitize', 'ngRoute', 'japodeOauth']);

japodeApp.config(function($routeProvider, $locationProvider) {

    $routeProvider.
            when('/', {
                redirectTo: '/blog'
            }).
            when('/japode', {
                templateUrl: 'http://' + jp_domain + '/template/welcome.html',
                controller: 'WelcomeController'
            }).
            when('/intro', {
                templateUrl: 'http://' + jp_domain + '/template/intro.html',
                controller: 'AddOrderController'
            }).
            when('/blog', {
                templateUrl: 'http://' + jp_domain + '/template/blog.html',
                controller: 'AddOrderController'
            }).
            when('/ghost', {
                templateUrl: '/pages/ghost.html',
                controller: 'AddOrderController'
            }).
                    
            when('/content', {
                template: '<div></div>',
                controller: function() {
                    window.location.replace('http://' + jp_domain + "/content");
                }
            }).
            when('/profile', {
                template: '<div></div>',
                controller: function() {
                    window.location.replace('http://' + jp_domain + "/profile");
                }
            }).
            when('/signout', {
                template: '<div></div>',
                controller: 'SignOutController'
            }).
            when('/rss', {
                templateURL: '/pages/rss.xml',
                controller: 'AddOrderController'
            }).
            otherwise({
                templateUrl: '/pages/detail.html',
                controller: 'DetailController'
            });


    // use the HTML5 History API
    $locationProvider.html5Mode(true);

});

japodeApp.config(function($sceDelegateProvider) {

    $sceDelegateProvider.resourceUrlWhitelist(['self', 'http://japode.prj.viglet.co/**']);

});

japodeApp.config(function(TokenProvider) {
    TokenProvider.extendConfig({
        clientId: 'demoapp',
        redirectUri: 'http://other.prj.viglet.co/demo.html', // allow lunching demo from a mirror
        state: ["8eb162ee707d7dcb1ac0940d05316110"]
    });
});

japodeApp.controller('DemoCtrl', function($rootScope, $scope, $window, Token) {
    $scope.accessToken = Token.get();
    $scope.authenticate = function() {
        var extraParams = $scope.askApproval ? {approval_prompt: 'force'} : {};
        Token.getTokenByPopup(extraParams)
                .then(function(params) {
                    $scope.accessToken = params.access_token;
                    $scope.expiresIn = params.expires_in;

                    Token.set(params.access_token);

                }, function() {
                    // Failure getting token from popup.
                    alert("Failed to get token from popup.");
                });
    };
    $scope.signoff = function() {
        Token.clear();
    };

});
japodeApp.controller('OAuthLoginController', function($scope, Token) {

    var extraParams = $scope.askApproval ? {approval_prompt: 'force'} : {};
    Token.getTokenByPopup(extraParams)
            .then(function(params) {
                $scope.accessToken = params.access_token;
                $scope.expiresIn = params.expires_in;

                Token.set(params.access_token);

            }, function() {
                // Failure getting token from popup.
                alert("Failed to get token from popup.");
            });

});
japodeApp.controller('SignOutController', function($scope, Token) {

    $scope.signout = Token.clear();
    window.location.replace('/');

});

japodeApp.controller('DetailController', function($scope, $http, $location) {

    var furl_service = 'http://' + jp_domain + '/services/furl' + $location.url() + "/" ;
    $http.get(furl_service).success(function(data) {
        $scope.post = data;
    });
    $scope.orderProp = 'date';
    $scope.checkURL = $location.url();
    
});

japodeApp.controller('WelcomeController', function($scope) {

    $scope.message = 'This is Add new order screen';

});
japodeApp.controller('AddOrderController', function($scope) {

    $scope.message = 'This is Add new order screen';

});


japodeApp.controller('ShowOrdersController', function($scope) {

    $scope.message = 'This is Show orders screen';

});
japodeApp.controller('PostListCtrl', function($scope, $http, $sce, Token) {
    $scope.accessToken = Token.get();
    var post_service = 'http://' + jp_domain + '/services/posts/';
    $http.get(post_service).success(function(data) {
        $scope.posts = data;
    });
    $scope.orderProp = 'date';
    $scope.jpTemplate = "postView";
    $scope.postURL = function(post_name) {
      return post_name.replace(/\ /g, "-");
    };
    $scope.postTemplate;

    $scope.init = function(posttypename, regionname, templatename)
    {
        $scope.postTemplate = templatename;
    };

    $scope.editPost = function($posttype, $post) {
        return $sce.trustAsResourceUrl('http://' + jp_domain + "/post/editorform/" + $posttype + "/" + $post + "/");
    };
    $scope.$on('$includeContentLoaded', function(event) {
        tinymce.init({
            selector: "textarea.editme"
        });
    }
    );
});

japodeApp.controller('PostCtrl', function($scope, $http, $sce, Token) {
    $scope.accessToken = Token.get();
    $scope.postTemplate;
    $scope.init = function(posttypename, regionname, templatename)
    {
        $scope.regionname = regionname;
        $scope.posttypename = posttypename;
        var post_service = $sce.trustAsResourceUrl('http://' + jp_domain + '/services/region/' + $scope.regionname + '/' + $scope.posttypename + '/');
        $http.get(post_service).success(function(data) {
            $scope.post = data;
        });
        $scope.postTemplate = templatename;
    };
    $scope.editPost = function($posttype, $post) {
        $scope.postTemplate = $sce.trustAsResourceUrl('http://' + jp_domain + "/post/editorform/" + $posttype + "/" + $post + "/");
        return $scope.postTemplate;
    };
    $scope.$on('$includeContentLoaded', function(event) {
        tinymce.init({
            selector: "textarea.editme"
        });
    }
    );
});

japodeApp.controller('PostTypeListCtrl', function($scope, $http, $sce) {
    var post_type_service = 'http://' + jp_domain + '/services/posttypes/';
    $http.get(post_type_service).success(function(data) {
        $scope.posttypes = data;
    });
    $scope.orderProp = 'name';
    $scope.changePostType = function($posttype) {
        $("#postEditForm").load($sce.trustAsResourceUrl('http://' + jp_domain + "/post/editorform/" + $posttype + "/"), function(response, status, xhr) {
            if (status == "error") {
                var msg = "Sorry but there was an error: ";
                $("#error").html(msg + xhr.status + " " + xhr.statusText);
            }
        });
    };
}
);
japodeApp.controller('WidgetListCtrl', function($scope, $http) {
    var widget_service = 'http://' + jp_domain + '/services/widgets/';
    $http.get(widget_service).success(function(data) {
        $scope.widgets = data;
    });
    $scope.orderProp = 'name';
}
);
japodeApp.controller('LabelListCtrl', function($scope, $http) {
    var label_service = 'http://' + jp_domain + '/services/labels.json';
    $http.get(label_service).success(function(data) {
        $scope.labels = data;
    });
    $scope.orderProp = 'hash';
}
);

japodeApp.controller('SiteListCtrl', function($scope, $http) {
    var site_service = 'http://' + jp_domain + '/services/sites/';
    $http.get(site_service).success(function(data) {
        $scope.sites = data;
    });
    $scope.orderProp = 'name';
}
);

/// Context editing

japodeApp.directive('jpRegion', function() {
    return {
        restrict: 'AEC',
        scope: {
            jpRgname: '@',
            jpPosttype: '@',
            jpTemplate: '@',
            jpList: '@'
        },
        templateUrl: function(element, attr) { 
            if (attr.jpList === 'true') {
              return 'http://' + jp_domain + '/template/jpList.html';
              //return attr.jpTemplate;
            }
            else {
                return 'http://' + jp_domain + '/template/jpRegion.html';
            }
        }
    }
});