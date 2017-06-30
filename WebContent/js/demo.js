'use strict';


angular.module('demo', ['japodeOauth']).
        config(function(TokenProvider) {
            // Demo configuration for the "angular-oauth demo" project on Google.
            // Log in at will!

            // Sorry about this way of getting a relative URL, powers that be.
            var baseUrl = document.URL.replace('intro/demo.html', '');

            TokenProvider.extendConfig({
                clientId: 'demoapp',
               // redirectUri: '/intro/oauth2callback.html', // allow lunching demo from a mirror
                redirectUri: 'http://other.prj.viglet.co/oauth2callback.html',
                state: ["8eb162ee707d7dcb1ac0940d05316110"]
            });
        }).
        controller('DemoCtrl', function($rootScope, $scope, $window, Token) {
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
        });
