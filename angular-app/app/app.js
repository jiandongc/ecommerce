var app = angular.module('store', [
	'ngRoute',
	'ngCookies',
	'home',
	'productDetail',
	'category',
	'customer',
	'cart',
	'checkout',
	'auth',
	'config'
]);

app.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
		otherwise({
			redirectTo: '/home'
		});
	}
]);


app.controller('appCtrl', function($scope, $cookies, $location, $rootScope, $http, $route, cartSummaryFactory, environment) {

	$scope.$watch(function() { return $cookies.get('current_user');}, function(newValue, oldValue) {
		if (typeof $scope.currentUser === "undefined" || newValue !== oldValue) {
			$scope.currentUser = $cookies.get('current_user');
		}
	})

	$scope.$watch(function() { return $cookies.get('customer_id');}, function(newValue, oldValue) {
		if (typeof $scope.customerId === "undefined" || newValue !== oldValue) {
			$scope.customerId = $cookies.get('customer_id');
		}
	})

	$scope.$watch(function() { return $cookies.get('cart_uid');}, function(newValue, oldValue) {
		if (typeof $scope.cartUid === "undefined" || newValue !== oldValue) {
			$scope.cartUid = $cookies.get('cart_uid');
			if(typeof $cookies.get('cart_uid') !== "undefined") {
				$rootScope.$broadcast('updateCartSummary', false);
			}
		}
	})

	$scope.$on('updateCartSummary', function(event, data) {
		cartSummaryFactory.get({
			cartuid: $cookies.get('cart_uid')
		}, function(response) {
			$scope.cartUid = $cookies.get('cart_uid');
			$scope.totalQuantity = response.totalQuantity;
			$scope.totalPrice = response.totalPrice;
			$scope.cartItems = response.cartItems;
			if(data === true){
				var dropdown = $('ul.nav li.dropdown').find('.dropdown-menu');
				dropdown.stop(true, true).fadeIn(1000, "swing", function(){
					dropdown.stop(true, true).delay(5000).fadeOut(3800);
				});
			}
		}, function(error){
			$scope.cartUid = null;
			$scope.totalQuantity = null;
			$scope.totalPrice = null;
			$scope.cartItems = null;
		});	
	})

	$scope.logout = function() {
		$cookies.remove('current_user');
		$cookies.remove('access_token');
		$cookies.remove('cart_uid');
		$cookies.remove('customer_id');
		$scope.cartItems = null;
		$location.path("#");
	}

	$scope.removeItem = function(cartItem){
		var configs = {headers: {'Content-Type' : 'application/json'}};
		$http.delete(environment.orderUrl + '/anoncarts/' + cartItem.cartUid + '/cartItems/' + cartItem.productId, configs).then(function(response){
			$rootScope.$broadcast('updateCartSummary', false);
			if($location.path().endsWith('/cart')){$route.reload();}
		}, function(error){
			alert('Delete failed');
		});
	}

	$scope.updateItem = function(cartItem){
		if (typeof cartItem.quantity === "undefined"){
			return;
		}

		var configs = {headers: {'Content-Type' : 'application/json'}};
		$http.patch(environment.orderUrl + '/anoncarts/' + cartItem.cartUid + '/items?productId=' + cartItem.productId, cartItem, configs).then(function(response){
			$rootScope.$broadcast('updateCartSummary', false);
			if($location.path().endsWith('/cart')){$route.reload();}
		}, function(error){
			alert(error.status);
			alert(error.data);
			alert('Update failed');
		});
	}

	$scope.isNotCheckOutPage = function(){
		if($location.path().indexOf('checkout') > -1){
			return false;	
		} else {
			return true;	
		}
	}
});