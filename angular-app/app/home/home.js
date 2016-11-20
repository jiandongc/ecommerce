var home = angular.module('home', ['ngRoute', 'ngCookies']);

home.service('productService', function($http, environment){
   this.getAllProducts = function() {
     return $http.get(environment.productUrl + '/products');
   };
});

home.controller('homeCtrl', function($scope, productService, cartService) {
	productService.getAllProducts().success(function(response) {
		$scope.processing = {};
		$scope.products = response;
		angular.forEach($scope.products,function(value,index){
			value.quantity = 1;
    	});
	});

  	$scope.addItemToCart = function(product){
    	cartService.addItem(product, function(){
      		$scope.processing[product.id]=false;
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
