var app = angular.module('store', [
	'ngRoute',
	'allProduct',
	'productDetail',
	'customer',
	'auth',
	'ngCookies'
]);

app.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
		otherwise({
			redirectTo: '/products'
		});
	}
]);


app.controller('appCtrl', function($scope, $cookies, $location, productsFactory) {

	$scope.$watch(function() { return $cookies.currentUser;}, function(newValue, oldValue) {
		if ($scope.currentUser === undefined || newValue !== oldValue) {
			$scope.currentUser = $cookies.currentUser;
		}
	})

	$scope.$on('updateCartSummary', function() {
		productsFactory.get({
			cartuid: $cookies.cart_uid
		}, function(response) {
			$scope.cartUid = $cookies.cart_uid;
			$scope.totalCount = response.totalCount;
			$scope.totalPrice = response.totalPrice;
		});
	})


	$scope.logout = function() {
		delete $cookies['currentUser'];
		delete $cookies['access_token'];
		delete $cookies['cart_uid'];
		$location.path("#");
	}
});