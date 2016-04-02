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


app.controller('appCtrl', function($scope, $cookies, $location, $rootScope, cartSummaryFactory) {

	$scope.$watch(function() { return $cookies.get('current_user');}, function(newValue, oldValue) {
		if (typeof $scope.currentUser === "undefined" || newValue !== oldValue) {
			$scope.currentUser = $cookies.get('current_user');
		}
	})

	$scope.$watch(function() { return $cookies.get('cart_uid');}, function(newValue, oldValue) {
		if (typeof $scope.cartUid === "undefined" || newValue !== oldValue) {
			$scope.cartUid = $cookies.get('cart_uid');
			if(typeof $cookies.get('cart_uid') !== "undefined") {
				$rootScope.$broadcast('updateCartSummaryByCartUid');
			}
		}
	})

	$scope.$on('updateCartSummaryByCartUid', function() {
		cartSummaryFactory.get({
			cartuid: $cookies.get('cart_uid')
		}, function(response) {
			$scope.cartUid = $cookies.get('cart_uid');
			$scope.totalCount = response.totalCount;
			$scope.totalPrice = response.totalPrice;
			$scope.cartItems = response.cartItems;
			var dropdown = $('ul.nav li.dropdown').find('.dropdown-menu');
			dropdown.stop(true, true).fadeIn(1000, "swing", function(){
				dropdown.stop(true, true).delay(5000).fadeOut(3800);
			});

		}, function(error){
			$scope.cartUid = null;
			$scope.totalCount = null;
			$scope.totalPrice = null;
		});	
	})

	$scope.logout = function() {
		$cookies.remove('current_user');
		$cookies.remove('access_token');
		$cookies.remove('cart_uid');
		$location.path("#");
	}
});