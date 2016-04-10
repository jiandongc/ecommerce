var allProducts = angular.module('allProduct', ['ngRoute', 'ngCookies']);

allProducts.service('allProductService', function($http){
   this.getAllProducts = function() {
     return $http.get('http://localhost:8080/products');
   }
});

allProducts.controller('allProductCtrl', function($scope, $cookies, $rootScope, allProductService, anonCartFactory) {

	allProductService.getAllProducts().success(function(response) {
		$scope.products = response;
		angular.forEach($scope.products,function(value,index){
			value.quantity = 1;
    	})
	});

	$scope.addItem = function(product){
        var anonCartItem = {
			productId : product.id,
			productName : product.name, 
			productPrice : product.unitPrice,
			quantity : product.quantity,
			imageUrl : product.imageUrl,
			cartUid : $cookies.get('cart_uid')
		};
		
		anonCartFactory.save(anonCartItem, function(data){
			$cookies.put('cart_uid', data.cartUid);
			$rootScope.$broadcast('updateCartSummary', true);
		});
	}
});

allProducts.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/products', {
        templateUrl: 'products/products.html',
        controller: 'allProductCtrl'
      });
}]);
