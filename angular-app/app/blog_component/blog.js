var blog = angular.module('blog', []);

blog.controller('blogCtrl', function(ngMeta) {
    ngMeta.setTitle('Our Blog');
});

blog.controller('blogArticleCtrl', function($scope, $routeParams) {
    $scope.article = $routeParams.article;
});

blog.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider
      .when('/blog', {
        templateUrl: 'blog_component/blog.html',
        controller: 'blogCtrl'
      }).when('/blogs/:article', {
        templateUrl: 'blog_component/blog-article.html',
        controller: 'blogArticleCtrl'
      });
}]);
