var leonor = angular.module('leonor', []);

leonor.directive('ngEnter', function() {
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


leonor.controller('NewsletterCtrl', function($scope, $http) {
    
     $scope.saveEmail = function() {
         
        $http({
            url: '/leonor2/api/newsletter/email/add/',
            method: "POST",
            data: $.param({email: $scope.lrEmail}),
            headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
        });
        // clean text field message
        $scope.lrEmail = "";
        new Messi('Seu email foi cadastrado :-)<br\> Em breve, receber&aacute; novidades sobre a Leonor',{width:'300px'});
    };
  });
 
