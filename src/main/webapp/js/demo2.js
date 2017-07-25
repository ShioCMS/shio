'use strict';


angular.module('demo', ['vecchioOauth']).

  config(function(TokenProvider) {
    // Demo configuration for the "angular-oauth demo" project on Google.
    // Log in at will!

    // Sorry about this way of getting a relative URL, powers that be.
    var baseUrl = document.URL.replace('example/demo.html', '');

    TokenProvider.extendConfig({
      clientId: '8d0304f37e2c0bbf1d9ab602e916ef34',
      redirectUri: 'http://localhost:8080/oauth2callback.html',  // allow lunching demo from a mirror
      scopes: ["https://www.googleapis.com/auth/userinfo.email"]
    });
  }).

  controller('DemoCtrl', function($rootScope, $scope, $window, Token) {
    $scope.accessToken = Token.get();

    $scope.authenticate = function() {
      var extraParams = $scope.askApproval ? {approval_prompt: 'force'} : {};
      Token.getTokenByPopup(extraParams)
        .then(function(params) {
          // Success getting token from popup.

          // Verify the token before setting it, to avoid the confused deputy problem.
          Token.verifyAsync(params.access_token).
            then(function(data) {
              $rootScope.$apply(function() {
                $scope.accessToken = params.access_token;
                $scope.expiresIn = params.expires_in;

                Token.set(params.access_token);
              });
            }, function() {
              alert("Failed to verify token.")
            });

        }, function() {
          // Failure getting token from popup.
          alert("Failed to get token from popup.");
        });
    };
  });
