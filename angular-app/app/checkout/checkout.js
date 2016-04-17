var checkout = angular.module('checkout',[]);

checkout.controller('deliveryCtrl', function() {
	
});

checkout.config(
  function($routeProvider) {
    $routeProvider.
      when('/checkout/delivery', {
        templateUrl: 'checkout/delivery.html',
        controller: 'deliveryCtrl'
      });
});