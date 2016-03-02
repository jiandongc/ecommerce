var cart = angular.module('cart',[]);

cart.factory('cartSummaryFactory', function($resource){
	return $resource('http://localhost:8082/anoncarts/summary/:cartuid');
});

cart.factory('anonCartFactory', function($resource){
	return $resource('http://localhost:8082/anoncarts/:cartuid');
});