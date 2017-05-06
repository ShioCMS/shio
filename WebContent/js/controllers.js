function PostListCtrl($scope, $http) {
  $http.get('services/posts.json').success(function(data) {
    $scope.posts = data;
  });
 
  $scope.orderProp = 'hash';
}

function LabelListCtrl($scope, $http) {
  $http.get('services/labels.json').success(function(data) {
    $scope.labels = data;
  });
 
  $scope.orderProp = 'hash';
}

