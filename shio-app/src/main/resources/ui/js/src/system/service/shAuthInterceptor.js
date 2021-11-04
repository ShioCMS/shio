shioApp.service('shAuthInterceptor', ['$q', function($q) {
    var service = this;

    service.responseError = function(response) {
        if (response.status == 401){
            window.location = "/welcome";
        }
        return $q.reject(response);
    };
}]);