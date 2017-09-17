var home = angular.module('home', ['ngRoute', 'ngCookies']);

home.service('productService', function($http, environment){
   this.getAllProducts = function() {
     return $http.get(environment.productUrl + '/products');
   };
});

home.controller('homeCtrl', function($scope, $http, environment, productService, cartService) {
  $scope.processing = {};

	$http.get(environment.productUrl + '/products/?cc=msg').success(function(response) {
		$scope.c1 = response;
		angular.forEach($scope.c1, function(value,index){
			value.quantity = 1;
    });
	});

  $http.get(environment.productUrl + '/products/?cc=mzg').success(function(response) {
    $scope.c2 = response;
    angular.forEach($scope.c2, function(value,index){
      value.quantity = 1;
    });
  });

  $scope.addItemToCart = function(product){
   	cartService.addItem(product, function(){
    	$scope.processing[product.code]=false;
    });
  };
});

home.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/home', {
        templateUrl: 'home/home.html',
        controller: 'homeCtrl'
      });
}]);
