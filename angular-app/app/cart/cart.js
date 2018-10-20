var cart = angular.module('cart',[]);

cart.controller('cartCtrl', function($scope, $cookies, $location, authService) {
  authService.assignGuestToken();

	// cartSummaryFactory.get({
	// 	cartuid: $cookies.get('cart_uid')
	// }, function(response) {
	// 	$scope.totalQuantity = response.totalQuantity;
	// 	$scope.totalPrice = response.totalPrice;
	// 	$scope.cartItems = response.cartItems;
 //    $scope.forms = {};
	// }, function(error){
	// 	$scope.totalQuantity = null;
	// 	$scope.totalPrice = null;
	// 	$scope.cartItems = null;
	// });

	$scope.checkout = function(){
    var valid = true;
    angular.forEach($scope.cartItems, function(value, index){
      var name = 'update_'+index;
      if($scope.forms[name].$invalid && valid == true){
        valid = false;
      }
    });

    if(valid == false){
      return;
    }

		if(typeof $cookies.get('current_user') === "undefined") {
			$location.path("/login");
		} else {
			$location.path("/shipping");
		}
	};
});

cart.factory('shoppingCartFactory', function($http, environment){
  var updateCustomerId = function(cartUid, customerId){
    return $http.put(environment.shoppingCartUrl + '/carts/' + cartUid, customerId).then(function(response){
      return response.data;
    });
  }

  var createShoppingCartForCustomer = function(customerId){
    return $http.post(environment.shoppingCartUrl + '/carts', customerId).then(function(response){
        return response.data;
    }); 
  }

  var createShoppingCartForGuest = function(){
    return $http.post(environment.shoppingCartUrl + '/carts').then(function(response){
        return response.data;
    }); 
  }

  var addItemToShoppingCart = function(name, price, imageUrl, sku, description, cartUid){
    var configs = {headers: {'Content-Type' : 'application/json'}};
    var cartItem = {
      name: name, 
      price: price,
      imageUrl: imageUrl,
      sku: sku,
      description: description
    };

    return $http.post(environment.shoppingCartUrl + '/carts/' + cartUid + '/items', cartItem, configs).then(function(response){
        return response.data;
    });
  }

  var getShoppingCart = function(cartUid){
      return $http.get(environment.shoppingCartUrl + '/carts/' + cartUid).then(function(response){
          return response.data;
      });   
  }

  var getShoppingCartByCustomerId = function(customerId){
      return $http.get(environment.shoppingCartUrl + '/carts/?customerId=' + customerId).then(function(response){
          return response.data;
      });
  }

  var deleteItemFromShoppingCart = function(cartUid, sku){
      return $http.delete(environment.shoppingCartUrl + '/carts/' + cartUid + '/items/' + sku).then(function(response){
          return response.data;
      }); 
  }

  return {
    updateCustomerId: updateCustomerId,
    addItemToShoppingCart: addItemToShoppingCart,
    createShoppingCartForCustomer: createShoppingCartForCustomer,
    createShoppingCartForGuest: createShoppingCartForGuest,
    getShoppingCart: getShoppingCart,
    getShoppingCartByCustomerId: getShoppingCartByCustomerId,
    deleteItemFromShoppingCart: deleteItemFromShoppingCart
  }
});

cart.config(
  function($routeProvider) {
    $routeProvider.
      when('/cart', {
        templateUrl: 'cart/cart.html',
        controller: 'cartCtrl'
      });

});