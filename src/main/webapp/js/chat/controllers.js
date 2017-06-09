var chatApp = angular.module('chatApp', ['angularMoment']);

chatApp.directive('ngEnter', function() {
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

chatApp.service('sharedProperties', function() {
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

chatApp.controller('UsersListCtrl', function($scope, $http, $timeout, sharedProperties) {
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

chatApp.controller('MessageCtrl', function($scope, $http, sharedProperties) {
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

chatApp.controller('MessagesLatestListCtrl', function($scope, $http, $timeout) {
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