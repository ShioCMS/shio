shioApp.controller('ShPlaygroundCtrl', [
    "$scope", "vigLocale", "$translate",
    function ($scope, vigLocale, $translate) {
        $scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
        $translate.use($scope.vigLanguage);
    }
]);