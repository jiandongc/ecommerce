var cart = angular.module('cart',[]);

cart.controller('cartCtrl', function($scope, $cookies, $location, cartSummaryFactory) {
	cartSummaryFactory.get({
		cartuid: $cookies.get('cart_uid')
	}, function(response) {
		$scope.totalQuantity = response.totalQuantity;
		$scope.totalPrice = response.totalPrice;
		$scope.cartItems = response.cartItems;
	}, function(error){
		$scope.totalQuantity = null;
		$scope.totalPrice = null;
		$scope.cartItems = null;
	});

	$scope.checkout = function() {
		if(typeof $cookies.get('current_user') === "undefined") {
			$location.path("/login");
		} else {
			$location.path("/checkout/delivery");
		}
		
	};
});

cart.factory('cartSummaryFactory', function($resource, environment){
	return $resource(environment.orderUrl + '/anoncarts/summary/:cartuid');
});

cart.factory('anonCartFactory', function($resource, environment){
	return $resource(environment.orderUrl + '/anoncarts/:cartuid');
});

cart.service('cartService', function($cookies, $rootScope, $timeout, anonCartFactory){
   this.addItem = function(product, callback){
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
      $timeout(callback, 1000);
    });
  };
});

cart.config(
  function($routeProvider) {
    $routeProvider.
      when('/cart', {
        templateUrl: 'cart/cart.html',
        controller: 'cartCtrl'
      });

});