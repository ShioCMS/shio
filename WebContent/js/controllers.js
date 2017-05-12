var japodeApp = angular.module('japodeApp', ['angularMoment', 'ngSanitize', 'ngRoute']);

japodeApp.controller('PostListCtrl', function($scope, $http, $sce) {

    var post_service = 'http://' + jp_domain + '/services/posts/';
    $http.get(post_service).success(function(data) {
        $scope.posts = data;
    });
    $scope.orderProp = 'date';
    $scope.jpTemplate = "postView";
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

japodeApp.controller('PostCtrl', function($scope, $http, $sce) {
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
        return $sce.trustAsResourceUrl('http://' + jp_domain + "/post/editorform/" + $posttype + "/" + $post + "/");
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
    $scope.teste1 = function() {
        window.alert("oi mundo");
    };
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
            jpTemplate: '@'
        },
        templateUrl: '/jp-templating/jpRegion.html'

    }
});

/// Chat

japodeApp.directive('ngEnter', function() {
    return function(scope, element, attrs) {
        element.bind("keydown keypress", function(event) {
            if (event.which === 13) {
                scope.$apply(function() {
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});

japodeApp.service('sharedProperties', function() {
    var stringValue = 'Todos';

    return {
        getString: function() {
            return stringValue;
        },
        setString: function(value) {
            stringValue = value;
        },
    }
});

japodeApp.controller('UsersListCtrl', function($scope, $http, $timeout, sharedProperties) {
    $scope.userActive = "Todos";

    var countUp = function() {
        $http.get('/chat/api/room/ID/user/list').success(function(data) {
            $scope.users = data.users;
        });

        $scope.orderProp = 'id';
        $timeout(countUp, 5000);
    }
    $timeout(countUp, 500);
    $scope.isActive = function($username) {
        if ($scope.userActive === $username)
            return true;
        else
            return false;
    }

    $scope.setUser = function($username) {
        sharedProperties.setString($username);
        $scope.userActive = sharedProperties.getString();
        document.getElementById('mensagem').focus();
    }
}
)

japodeApp.controller('MessageCtrl', function($scope, $http, sharedProperties) {
    $scope.sgMessage = "";
    $scope.sgReserved = false;
    $scope.sgColor = "#006699";
    $scope.sgWho = sharedProperties.getString();
    $scope.sgType = "separador";
    $scope.sgNick = document.getElementById("nick").value;



    $scope.sendMessage = function() {

        $scope.sgWho = sharedProperties.getString();

        //mostra o texto antes de enviar
        var newdiv = document.createElement("div");
        newdiv.className = $scope.sgType;
        newdiv.innerHTML = '<b><font color="' + $scope.sgColor + '">' + $scope.sgNick + '</font></b> <i><font color="#666666">' + (($scope.sgReserved === true) ? "reservadamente" : "") + ' fala com</font></i> <b>' + $scope.sgWho + '</b>:<br>' + $scope.sgMessage + '<br>';
        document.getElementById("mostrar").appendChild(newdiv);

        //envia a mensagem IMPORTANTE: Precisa converter em JSON o request
        $http({
            url: '/chat/api/room/ID/message/ID_USER/',
            method: "POST",
            data: $.param({mensagem: $scope.sgMessage, falacom: $scope.sgWho, reser: $scope.sgReserved, cor: $scope.sgColor}),
            headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
        });
        // clean text field message
        $scope.sgMessage = "";
    };
}
)

japodeApp.controller('MessagesLatestListCtrl', function($scope, $http, $timeout) {
    $scope.sgReserved = false;
    $scope.sgColor = "#006699";
    $scope.sgType = "separador";
    $scope.sgNick = document.getElementById("nick").value;
    
    var countUp = function() {
        $http.get('/chat/api/room/ID/message/lastest').success(function(data) {
            $scope.messages = data.messages;
        });

        $scope.orderProp = 'name';

/*        $scope.messages.forEach(function(message, i) {
            alert("Ale1" + message.name);
            var newdiv = document.createElement("div");
            newdiv.className = "separador";
            newdiv.innerHTML = "Testando123";
            document.getElementById("mostrar").appendChild(newdiv);

        })
  */      
        angular.forEach($scope.messages, function(message, key) {
            var newdiv = document.createElement("div");
            newdiv.className = "separador";
            newdiv.innerHTML = '<b><font color="' + $scope.sgColor + '">' + message.name + '</font></b> <i><font color="#666666">' + ((message.reserved === true) ? "reservadamente" : "") + ' fala com</font></i> <b>' + message.name + '</b>:<br>' + message.message + '<br>';
            document.getElementById("mostrar").appendChild(newdiv);
  });


        $timeout(countUp, 5000);
    }
    $timeout(countUp, 500);
}
)