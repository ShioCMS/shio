'use strict';

/**
 * A module to include instead of `angularOauth` for a service preconfigured
 * for Google OAuth authentication.
 *
 * Guide: https://developers.google.com/accounts/docs/OAuth2UserAgent
 */
angular.module('japodeOauth', ['angularOauth']).
        constant('JapodeTokenVerifier', function (config, accessToken) {
            var $injector = angular.injector(['ng']);
            return $injector.invoke(['$http', '$rootScope', '$q', function ($http, $rootScope, $q) {
                    var deferred = $q.defer();
                    var verificationEndpoint = 'http://japode.prj.viglet.co/lockdin/resource';

                    $rootScope.$apply(function () {
                        $http({method: 'POST', url: verificationEndpoint, params: {access_token: accessToken}}).
                                success(function (data) {
                                    if (data.audience == config.clientId) {
                                        deferred.resolve(data);
                                    } else {
                                        deferred.reject({name: 'invalid_audience'});
                                    }
                                }).
                                error(function (data, status, headers, config) {
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
        config(function (TokenProvider, JapodeTokenVerifier) {
            TokenProvider.extendConfig({
                authorizationEndpoint: 'http://japode.prj.viglet.co/lockdin/authorize',
                state: '8eb162ee707d7dcb1ac0940d05316110',
                verifyFunc: JapodeTokenVerifier
            });
        });

