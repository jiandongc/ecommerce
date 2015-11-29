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

	$scope.hasRefreshed = false;

	$scope.$watch(function() {
		if (!$scope.hasRefreshed) {
			$scope.cartUid = $cookies.cart_uid;
			if ($cookies.cart_uid !== undefined) {
				productsFactory.get({cartuid: $cookies.cart_uid}, function(response) {
					$scope.totalCount = response.totalCount;
					$scope.totalPrice = response.totalPrice;
				});
			}
			$scope.hasRefreshed = true;
		}

		$scope.$$postDigest(function() {
			$scope.hasRefreshed = false;
		});
	})


	$scope.logout = function() {
		delete $cookies['currentUser'];
		delete $cookies['access_token'];
		delete $cookies['cart_uid'];
		$location.path("#");
	}
});