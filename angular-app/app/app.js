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

app.controller('appCtrl', function($scope, $location, $localstorage, $rootScope, shoppingCartFactory) {
	$scope.$watch(function() { return $localstorage.get('current_user', undefined);}, function(newValue, oldValue) {
		if (typeof $scope.currentUser === "undefined" || newValue !== oldValue) {
			$scope.currentUser = newValue;
		}
	})

	$scope.$watch(function() { return $localstorage.get('cart_uid', undefined);}, function(newValue, oldValue) {
		if (typeof $scope.cartUid === "undefined" || newValue !== oldValue) {
			$scope.cartUid = $localstorage.get('cart_uid', undefined);
			if(typeof $localstorage.get('cart_uid', undefined) !== "undefined") {
				$rootScope.$broadcast('updateCartSummary', false);
			}
		}
	})

	$scope.$on('updateCartSummary', function(event, showDropDown) {
		shoppingCartFactory.getShoppingCart($localstorage.get('cart_uid')).then(function(data){
			$scope.cartUid = data.shoppingCart.cartUid;
			$scope.totalQuantity = data.totalQuantity;
			$scope.itemsSubTotal = data.itemsSubTotal;
			$scope.cartItems = data.shoppingCart.shoppingCartItems;
			if(showDropDown === true){
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

	$scope.$on('authenticationFaield', function(event, args) {
		$localstorage.clear();
		$scope.cartUid = null;
		$scope.totalQuantity = null;
		$scope.totalPrice = null;
		$scope.cartItems = null;		
	})

	$scope.logout = function() {
		$localstorage.clear();
		$scope.cartUid = null;
		$scope.totalQuantity = null;
		$scope.totalPrice = null;
		$scope.cartItems = null;
		$location.path("#");
	}

	$scope.removeItem = function(cartItem){
		// var configs = {headers: {'Content-Type' : 'application/json'}};
		// $http.delete(environment.orderUrl + '/anoncarts/' + cartItem.cartUid + '/cartItems/' + cartItem.productId, configs).then(function(response){
		// 	$rootScope.$broadcast('updateCartSummary', false);
		// 	if($location.path().endsWith('/cart')){$route.reload();}
		// }, function(error){
		// 	alert('Delete failed');
		// });
	}

	$scope.updateItem = function(cartItem){
		// if (typeof cartItem.quantity === "undefined"){
		// 	return;
		// }

		// var configs = {headers: {'Content-Type' : 'application/json'}};
		// $http.patch(environment.orderUrl + '/anoncarts/' + cartItem.cartUid + '/items?productId=' + cartItem.productId, cartItem, configs).then(function(response){
		// 	//$rootScope.$broadcast('updateCartSummary', false);
		// 	if($location.path().endsWith('/cart')){$route.reload();}
		// }, function(error){
		// 	alert(error.status);
		// 	alert(error.data);
		// 	alert('Update failed');
		// });
	}

	$scope.isNotCheckOutPage = function(){
		if($location.path().indexOf('checkout') > -1){
			return false;	
		} else {
			return true;	
		}
	}
});

app.factory('$localstorage', ['$window', function($window) {
  return {
    set: function(key, value) {
      $window.localStorage[key] = value;
    },
    get: function(key, defaultValue) {
      return $window.localStorage[key] || defaultValue || false;
    },
    setObject: function(key, value) {
      $window.localStorage[key] = JSON.stringify(value);
    },
    getObject: function(key, defaultValue) {
      if($window.localStorage[key] != undefined){
          return JSON.parse($window.localStorage[key]);
      }else{
        return defaultValue || false;
      }
    },
    remove: function(key){
      $window.localStorage.removeItem(key);
    },
    clear: function(){
      $window.localStorage.clear();
    },
    containsKey: function(key){
    	return $window.localStorage[key] != undefined;
    }
  }
}]);

app.factory('accessTokenInterceptor', function($localstorage, $location, $q){
	var service = this;

    service.request = function(config) {
    	if($localstorage.containsKey('access_token')) {
    		config.headers.Authentication = $localstorage.get('access_token');	
    	}
    	return config;
    };

    service.responseError = function(response) {
        if (response.status === 401) {
        	$location.path("/login");
        }
        return $q.reject(response);
    };

    return service;
});

app.config(function($routeProvider, $httpProvider){
	$routeProvider.otherwise({redirectTo: '/home'});
	$httpProvider.interceptors.push('accessTokenInterceptor');
});