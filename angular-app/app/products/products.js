var allProducts = angular.module('allProduct', ['ngRoute', 'ngCookies']);

allProducts.service('allProductService', function($http){
   this.getAllProducts = function() {
     return $http.get('http://localhost:8080/products');
   }
});

allProducts.controller('allProductCtrl', function($scope, allProductService, productsFactory, authService, $cookies) {

	allProductService.getAllProducts().success(function(response) {
		$scope.products = response;
		angular.forEach($scope.products,function(value,index){
			value.quantity = 1;
    	})
	});

	$scope.addItem = function(product){
		authService.authorize(function(){
            var anonCartItem = {
				productId : product.id,
				productName : product.name,
				productPrice : product.unitPrice,
				quantity : product.quantity
			};
			productsFactory.save(anonCartItem, function(data){
				$cookies.cart_uid = data.cartUid;
			});
        });
	}
});

allProducts.factory('productsFactory', function($resource){
	return $resource('http://localhost:8082/anoncarts/:id');
});

allProducts.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/products', {
        templateUrl: 'products/products.html',
        controller: 'allProductCtrl'
      });
}]);
