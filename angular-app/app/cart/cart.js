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
		
	}
});

cart.factory('cartSummaryFactory', function($resource){
	return $resource('http://localhost:8082/anoncarts/summary/:cartuid');
});

cart.factory('anonCartFactory', function($resource){
	return $resource('http://localhost:8082/anoncarts/:cartuid');
});

cart.config(
  function($routeProvider) {
    $routeProvider.
      when('/cart', {
        templateUrl: 'cart/cart.html',
        controller: 'cartCtrl'
      });

});