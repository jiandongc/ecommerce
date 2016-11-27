var checkout = angular.module('checkout',[]);

checkout.controller('checkoutCtrl', function() {
	
});

checkout.config(
  function($routeProvider) {
    $routeProvider.
      when('/checkout', {
        templateUrl: 'checkout/checkout.html',
        controller: 'checkoutCtrl'
      });
});