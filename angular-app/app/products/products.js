var allProducts = angular.module('allProduct', ['ngRoute']);

allProducts.service('allProductService', function($http){
   this.getAllProducts = function() {
     return $http.get('http://localhost:8080/products');
   }
});

allProducts.controller('allProductCtrl', function($scope, allProductService) {
  allProductService.getAllProducts().success(function(response) {$scope.products = response;});
});

allProducts.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/products', {
        templateUrl: 'products/products.html',
        controller: 'allProductCtrl'
      });
}]);