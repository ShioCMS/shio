'use strict';

/**
 * A module to include instead of `angularOauth` for a service preconfigured
 * for Google OAuth authentication.
 *
 * Guide: https://developers.google.com/accounts/docs/OAuth2UserAgent
 */
angular.module('vecchioOauth', ['angularOauth']).

  constant('GoogleTokenVerifier', function(config, accessToken) {
    var $injector = angular.injector(['ng']);
    return $injector.invoke(['$http', '$rootScope', '$q', function($http, $rootScope, $q) {
      var deferred = $q.defer();
      var verificationEndpoint = 'http://localhost:2702/api/tokeninfo';
    console.log("Etapa 01");
      $rootScope.$apply(function() {
        $http({method: 'GET', url: verificationEndpoint, params: {access_token: accessToken}}).        
          then(function(response) {
            console.log("Etapa 02");
            console.log("Etapa 02a " + response.data.id);
            console.log("Etapa 02b " + response.data.id.clientId);
            console.log("Etapa 02c " + config.clientId);
            if (response.data.id.clientId == config.clientId) {
                console.log("Etapa 03 " + response.data.clientId);
              deferred.resolve(response.data);
            } else {
              deferred.reject({name: 'invalid_audience'});
            }
          },
          function(data, status, headers, config) {
            deferred.reject({
              name: 'error_response',
              data: data,
              status: status,
              headers: headers,
              config: config
            });
          });
      });

      return deferred.promise;
    }]);
  }).

  config(function(TokenProvider, GoogleTokenVerifier) {
    TokenProvider.extendConfig({
      authorizationEndpoint: 'http://localhost:2702/login',
      scopes: ["https://www.googleapis.com/auth/userinfo.email"],
      verifyFunc: GoogleTokenVerifier
    });
  });

