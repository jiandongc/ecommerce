var home = angular.module('home', ['ngRoute', 'ngCookies']);

home.service('productService', function($http, environment){
   this.getAllProducts = function() {
     return $http.get(environment.productUrl + '/products');
   };
});

home.controller('homeCtrl', function($scope, $cookies, $rootScope, productService, anonCartFactory) {
	productService.getAllProducts().success(function(response) {
		$scope.products = response;
		angular.forEach($scope.products,function(value,index){
			value.quantity = 1;
    	});
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
