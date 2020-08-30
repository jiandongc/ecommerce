var blog = angular.module('blog', []);

blog.controller('blogCtrl', function($window) {
  $window.document.title = 'Our Blog | Noodle Monster';
});

blog.controller('blogArticleCtrl', function($scope, $routeParams) {
  $scope.article = $routeParams.article;
});

blog.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider
      .when('/blog', {
        templateUrl: 'blog/blog.html',
        controller: 'blogCtrl'
      }).when('/blogs/:article', {
        templateUrl: 'blog/blog-article.html',
        controller: 'blogArticleCtrl'
      });
}]);