var blog = angular.module('blog', []);

blog.controller('blogCtrl', function(ngMeta) {
    ngMeta.setTitle('Our Blog');
});

blog.controller('blogsCtrl', function($scope, $routeParams, blogFactory, ngMeta) {
    $scope.loading = true;
    blogFactory.getAllPublishedPosts().then(function(response){
        $scope.posts = response;
      	$scope.loading = false;
      	ngMeta.setTitle("Blog");
    });
});

blog.controller('blogCategoryCtrl', function($scope, $routeParams, blogFactory, ngMeta) {
    $scope.loadingCategories = true;
    blogFactory.getCategory($routeParams.slug).then(function(response){
        $scope.category = response;
      	$scope.loadingCategories = false;
      	ngMeta.setTitle($scope.category.name + ', 英国');
    });

    $scope.loadingPosts = true;
    blogFactory.getAllPublishedPostsByCategory($routeParams.slug).then(function(response){
        $scope.posts = response;
      	$scope.loadingPosts = false;
    });
});


blog.controller('blogArticleCtrl', function($scope, $routeParams, $sce, blogFactory, environment, ngMeta) {
    $scope.loadingPost = true;
    blogFactory.getPost($routeParams.slug).then(function(response){
        $scope.post = response;
        $scope.loadingPost = false;

        $scope.postUrl = environment.blogPage + "/" + $scope.post.slug;
        $scope.facebookUrl = $sce.trustAsResourceUrl('https://www.facebook.com/plugins/like.php?href='+$scope.postUrl+'&width=138&layout=button&action=like&size=large&share=true&height=65&appId');
      	ngMeta.setTitle($scope.post.title);
      	ngMeta.setTag('description', $scope.post.summary);
        ngMeta.setTag('ogUrl', $scope.postUrl);
        ngMeta.setTag('ogType', 'article');
        ngMeta.setTag('ogTitle', $scope.post.title);
        ngMeta.setTag('ogDescription', $scope.post.summary);
        ngMeta.setTag('ogImage', $scope.post.image);

        $scope.postJsonLd = {
            "@context":"http://schema.org",
            "@type":"NewsArticle",
            "headline": $scope.post.summary,
            "image": [$scope.post.image],
            "datePublished": $scope.post.createDate.year + '-' + $scope.post.createDate.monthValue + '-' + $scope.post.createDate.dayOfMonth + 'T00:00:00+00:00',
            "dateModified": $scope.post.updateDate != null ? $scope.post.updateDate.year + '-' + $scope.post.updateDate.monthValue + '-' + $scope.post.updateDate.dayOfMonth + 'T00:00:00+00:00' : null
        }
        console.log(JSON.stringify($scope.postJsonLd));
        $('#post-json-ld').html(JSON.stringify($scope.postJsonLd));

    });
});

blog.factory('blogFactory', function($http, environment) {
    var getAllPublishedPosts = function() {
        return $http.get(environment.productUrl + '/posts').then(function(response) {
            return response.data;
        });
    }

    var getAllPublishedPostsByCategory = function(slug) {
        return $http.get(environment.productUrl + '/posts?category=' + slug).then(function(response) {
            return response.data;
        });
    }

    var getPost = function(slug) {
        return $http.get(environment.productUrl + '/posts/' + slug).then(function(response) {
            return response.data;
        });
    }

    var getCategory = function(slug) {
        return $http.get(environment.productUrl + '/posts/categories/' + slug).then(function(response) {
            return response.data;
        });
    }

    return {
        getAllPublishedPosts: getAllPublishedPosts,
        getAllPublishedPostsByCategory: getAllPublishedPostsByCategory,
        getPost: getPost,
        getCategory: getCategory
    }
});

blog.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider
      .when('/blog', {
        templateUrl: 'blog_component/blog.html',
        controller: 'blogCtrl'
      }).when('/blogs', {
        templateUrl: 'blog_component/blogs.html',
        controller: 'blogsCtrl'
      }).when('/blogs/:slug', {
        templateUrl: 'blog_component/blog-article.html',
        controller: 'blogArticleCtrl'
      }).when('/post-category/:slug', {
        templateUrl: 'blog_component/blog-category.html',
        controller: 'blogCategoryCtrl'
      });
}]);
