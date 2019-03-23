var cart = angular.module('cart',[]);

cart.controller('cartCtrl', function($scope, $rootScope, authService, shoppingCartFactory, $localstorage, $timeout, $location) {

  $scope.template.header = 'cart-header.html';
  $scope.template.footer = 'default-footer.html';

  authService.assignGuestToken();

  $scope.updateItem = function(cartItem){
    if(cartItem.quantity == undefined){
        $timeout(function() {
          $rootScope.$broadcast('updateCartSummary', false);
        }, 3000);
    } else {
        var cartUid = $localstorage.get('cart_uid');
        shoppingCartFactory.updateShoppingCartItem(cartUid, cartItem).then(function(response){
            $rootScope.$broadcast('updateCartSummary', false);
        });
    }
  }

	$scope.checkout = function(){
		if(typeof $localstorage.get('cart_uid', undefined) !== "undefined") {
			$location.path("/shipping");
		} else {
			$location.path("/login");
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

  var addItemToShoppingCart = function(name, code, price, imageUrl, sku, description, cartUid){
    var cartItem = {
      name: name, 
      code: code,
      price: price,
      imageUrl: imageUrl,
      sku: sku,
      description: description
    };

    return $http.post(environment.shoppingCartUrl + '/carts/' + cartUid + '/items', cartItem).then(function(response){
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

  var updateShoppingCartItem = function(cartUid, cartItem){
      return $http.put(environment.shoppingCartUrl + '/carts/' + cartUid + '/items', cartItem).then(function(response){
        return response.data;
      });   
  }

  var addAddress = function(cartUid, address) {
      return $http.post(environment.shoppingCartUrl + '/carts/' + cartUid + '/addresses', address).then(function(response){
        return response.data;
      }); 
  };

  return {
    updateCustomerId: updateCustomerId,
    addItemToShoppingCart: addItemToShoppingCart,
    createShoppingCartForCustomer: createShoppingCartForCustomer,
    createShoppingCartForGuest: createShoppingCartForGuest,
    getShoppingCart: getShoppingCart,
    getShoppingCartByCustomerId: getShoppingCartByCustomerId,
    deleteItemFromShoppingCart: deleteItemFromShoppingCart,
    updateShoppingCartItem: updateShoppingCartItem,
    addAddress: addAddress
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