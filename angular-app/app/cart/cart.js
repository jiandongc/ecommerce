var cart = angular.module('cart',[]);

cart.factory('cartSummaryFactory', function($resource){
	return $resource('http://localhost:8082/anoncarts/summary/:cartuid');
});