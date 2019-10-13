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
	'config',
	'order',
	'productTag'
]);

app.controller('appCtrl', function($scope, $location, $localstorage, $rootScope, shoppingCartFactory, categoryFactory, authFactory) {

    $scope.template = {header: "default-header.html", footer: "default-footer.html"};

	$scope.$watch(function() { return $localstorage.get('current_user');}, function(newValue, oldValue) {
		if (typeof $scope.currentUser === "undefined" || newValue !== oldValue) {
			$scope.currentUser = newValue;
		}
	})

	$scope.$watch(function() { return $localstorage.get('customer_id');}, function(newValue, oldValue) {
		if (typeof $scope.customerId === "undefined" || newValue !== oldValue) {
			$scope.customerId = newValue;
		}
	})

	$scope.$watch(function() { return $localstorage.get('cart_uid');}, function(newValue, oldValue) {
		if (typeof $scope.cartUid === "undefined" || newValue !== oldValue) {
			$scope.cartUid = newValue;
			if($localstorage.get('cart_uid') !== false) {
				$rootScope.$broadcast('updateCartSummary', false);
			}
		}
	})

	$scope.$watch(function() { return $localstorage.get('access_token');}, function(newValue, oldValue) {
		if ($localstorage.get('access_token') === false) {
			$rootScope.$broadcast('downloadGuestToken');
		}
	})

	$scope.$on('updateCartSummary', function(event, showDropDown) {
		shoppingCartFactory.getShoppingCart($localstorage.get('cart_uid')).then(function(data){
			$scope.cartUid = data.cartUid;
			$scope.totalQuantity = (data.quantity != 0 ? data.quantity : null);
			$scope.itemsTotal = (data.itemsTotal != 0 ? data.itemsTotal : null);
			$scope.cartItems = data.cartItems;
			if(showDropDown === true){
				var dropdown = $('ul.menu li.dropdown').find('.dropdown-menu');
				dropdown.stop(true, true).fadeIn(1000, "swing", function(){
					dropdown.stop(true, true).delay(5000).fadeOut(3800);
				});
			}
		}, function(error){
			$scope.cartUid = null;
			$scope.totalQuantity = null;
			$scope.itemsTotal = null;
			$scope.cartItems = null;
		});
	})

	$scope.$on('reset', function(event, args) {
		$localstorage.clear();
		$scope.cartUid = null;
		$scope.totalQuantity = null;
		$scope.itemsTotal = null;
		$scope.cartItems = null;
	})

 	$scope.$on('downloadGuestToken', function(event, args) {
   		authFactory.downloadGuestToken().then(function(response){
   			$localstorage.set('access_token', response.headers("Authentication"));
   			$rootScope.$broadcast('initialiseData');
   		});
 	});

	$scope.$on('$routeChangeStart', function($event, next, current) { 
   		$scope.template.header = 'default-header.html';
		$scope.template.footer = 'default-footer.html';
 	});

	$scope.$on('initialiseData', function(event, args) { 
		categoryFactory.getSubCategories("ls", 2).then(function(response){
			$scope.categoryOne = response;
		});

		categoryFactory.getSubCategories("yl", 2).then(function(response){
			$scope.categoryTwo = response;
		});
 	});

	$scope.removeItem = function(cartItem){
		shoppingCartFactory.deleteItemFromShoppingCart($scope.cartUid, cartItem.sku).then(function(response){
			$rootScope.$broadcast('updateCartSummary', false);
		});
	}

	$rootScope.$broadcast('initialiseData');

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
        if (response.status === 400 || response.status === 401 || response.status === 403) {
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