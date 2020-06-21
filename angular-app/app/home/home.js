var home = angular.module('home', ['ngRoute', 'ngCookies']);

home.controller('homeCtrl', function($rootScope, $scope, tagFactory, brandFactory) {
  $rootScope.$broadcast('initialiseData');

  $scope.loadingTags = true;
  tagFactory.getAllTags().then(function(response){
  	$scope.tags = response;
  	$scope.loadingTags = false;
  });

  $scope.loadingBrands = true;
  brandFactory.getAllBrands().then(function(response){
  	$scope.brands = response;
  	for (var i in $scope.brands) {
        $scope.brands[i].imageUrl = $scope.brands[i].imageUrl ? $scope.brands[i].imageUrl : '/images/brand/notfound.png';
    }
    $scope.loadingBrands = false;
  });
});

home.component('productpanel', {
  templateUrl: 'component/product-panel.html',
  controller: function($scope, $element, productFactory){
    $scope.tag = $element.attr("tag");

    productFactory.getProductsWithTag($scope.tag).then(function(response){
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
  bindings: {title: '@', color: '@', icon: '@'}
});

home.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/home', {
        templateUrl: 'home/home.html',
        controller: 'homeCtrl'
      });
}]);
