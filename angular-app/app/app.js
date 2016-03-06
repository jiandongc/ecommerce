var app = angular.module('store', [
	'ngRoute',
	'allProduct',
	'productDetail',
	'customer',
	'cart',
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


app.controller('appCtrl', function($scope, $cookies, $location, cartSummaryFactory) {

	$scope.$watch(function() { return $cookies.get('current_user');}, function(newValue, oldValue) {
		if (typeof $scope.currentUser === "undefined" || newValue !== oldValue) {
			$scope.currentUser = $cookies.get('current_user');
		}
	})

	$scope.$on('updateCartSummary', function() {
		cartSummaryFactory.get({
			cartuid: $cookies.get('cart_uid')
		}, function(response) {
			$scope.cartUid = $cookies.get('cart_uid');
			$scope.totalCount = response.totalCount;
			$scope.totalPrice = response.totalPrice;
		});
	})


	$scope.logout = function() {
		$cookies.remove('current_user');
		$cookies.remove('access_token');
		$cookies.remove('cart_uid');
		$location.path("#");
	}
});