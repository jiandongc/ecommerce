var home = angular.module('home', ['ngRoute', 'ngCookies']);

home.controller('homeCtrl', function($scope, tagFactory, brandFactory, blogFactory) {
  $scope.loadingTags = true;
  tagFactory.getAllTags().then(function(response){
  	$scope.tags = response;
  	$scope.loadingTags = false;
  });

  $scope.loadingBrands = true;
  brandFactory.getAllBrandsInRandomOrder().then(function(response){
  	$scope.brands = response;
  	for (var i in $scope.brands) {
        $scope.brands[i].imageUrl = $scope.brands[i].imageUrl ? $scope.brands[i].imageUrl : '/images/brand/notfound.png';
    }
    $scope.loadingBrands = false;
  });

  $scope.loadingPosts = true;
  blogFactory.getAllPublishedPosts().then(function(response){
  	$scope.posts = response;
  	$scope.loadingPosts = false;
  });

});

home.component('productpanel', {
  templateUrl: 'component/product-panel.html',
  controller: function($scope, $element, productFactory){
    $scope.tag = $element.attr("tag");

    productFactory.getProductsWithTagInOrder($scope.tag, 'random').then(function(response){
        $scope.products = response;
    });

    $scope.refresh = function(categoryCode){
      $scope.loading = 'opacity-05';
      if(categoryCode == undefined){
          productFactory.getProductsWithTag($scope.tag).then(function(response){
              $scope.loading = '';
              $scope.products = response;
          });
      } else {
          productFactory.getProductsWithTagInCategory($scope.tag, categoryCode).then(function(response){
              $scope.loading = '';
              $scope.products = response;
          });
      }
    }
  },
  bindings: {title: '@', color: '@', icon: '@', categories: '='}
});

home.controller('contactUsCtrl', function($scope, ngMeta) {
    ngMeta.setTitle('与我们联系 | Contact Information');
});

home.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'home_component/home.html',
        controller: 'homeCtrl'
      }).when('/contact-us', {
         templateUrl: 'home_component/contact-us.html',
         controller: 'contactUsCtrl'
      });
}]);
