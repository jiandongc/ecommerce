var cart = angular.module('cart', []);

cart.controller('cartCtrl', function($scope, $rootScope, shoppingCartFactory, $localstorage, $timeout, $location, $routeParams) {

    $scope.template.header = 'cart-header.html';
    $scope.template.footer = 'default-footer.html';

    if($routeParams.cartUid != null){
        $localstorage.set('cart_uid', $routeParams.cartUid);
        $rootScope.$broadcast('updateCartSummary', false);
    }

    $scope.updateItem = function(cartItem) {
        if (cartItem.quantity == undefined) {
            $timeout(function() {
                $rootScope.$broadcast('updateCartSummary', false);
            }, 3000);
        } else {
            var cartUid = $localstorage.get('cart_uid');
            shoppingCartFactory.updateShoppingCartItem(cartUid, cartItem).then(function(response) {
                $rootScope.$broadcast('updateCartSummary', false);
            });
        }
    }

    $scope.checkout = function() {
        if ($localstorage.containsKey('customer_id')) {
            $scope.checking = true;
            shoppingCartFactory.getShoppingCart($localstorage.get('cart_uid')).then(function(response){
                $scope.shoppingCart = response;
                if ($scope.shoppingCart.shipping == null) {
                    $location.path("/checkout/shipping");
                } else if ($scope.shoppingCart.deliveryOption == null){
                    $location.path("/checkout/delivery");
                } else if ($scope.shoppingCart.billing == null){
                    $location.path("/checkout/billing");
                }  else {
                    $location.path("/checkout/payment");
                }
            });
        } else {
            $scope.checking = true;
            shoppingCartFactory.getShoppingCart($localstorage.get('cart_uid')).then(function(response){
                $scope.shoppingCart = response;
                $location.path("/login");
            });
            
        }
    };
});

cart.factory('shoppingCartFactory', function($http, $localstorage, environment) {
    var addCustomerInfo = function(cartUid, customerData) {
        return $http.put(environment.shoppingCartUrl + '/carts/' + cartUid, customerData).then(function(response) {
            return response.data;
        });
    }

    var createShoppingCartForCustomer = function(customerData) {
        return $http.post(environment.shoppingCartUrl + '/carts', customerData).then(function(response) {
            return response.data;
        });
    }

    var createShoppingCartForGuest = function() {
        return $http.post(environment.shoppingCartUrl + '/carts').then(function(response) {
            return response.data;
        });
    }

    var addItemToShoppingCart = function(name, code, price, imageUrl, sku, description, cartUid, vatRate) {
        var cartItem = {
            name: name,
            code: code,
            price: price,
            imageUrl: imageUrl,
            sku: sku,
            description: description,
            vatRate: vatRate
        };

        return $http.post(environment.shoppingCartUrl + '/carts/' + cartUid + '/items', cartItem).then(function(response) {
            return response.data;
        });
    }

    var getShoppingCart = function(cartUid) {
        return $http.get(environment.shoppingCartUrl + '/carts/' + cartUid).then(function(response) {
            return response.data;
        }, function(error){
            $localstorage.remove('cart_uid');
        });
    }

    var getShoppingCartByCustomerId = function(customerId) {
        return $http.get(environment.shoppingCartUrl + '/carts?customerId=' + customerId).then(function(response) {
            return response.data;
        });
    }

    var deleteItemFromShoppingCart = function(cartUid, sku) {
        return $http.delete(environment.shoppingCartUrl + '/carts/' + cartUid + '/items/' + sku).then(function(response) {
            return response.data;
        });
    }

    var updateShoppingCartItem = function(cartUid, cartItem) {
        return $http.put(environment.shoppingCartUrl + '/carts/' + cartUid + '/items', cartItem).then(function(response) {
            return response.data;
        });
    }

    var addAddress = function(cartUid, address) {
        return $http.post(environment.shoppingCartUrl + '/carts/' + cartUid + '/addresses', address).then(function(response) {
            return response.data;
        });
    };

    var addDeliveryOption = function(cartUid, deliveryOption) {
        return $http.post(environment.shoppingCartUrl + '/carts/' + cartUid + '/deliveryoption', deliveryOption).then(function(response) {
            return response.data;
        });
    };

    var getDeliveryOption = function(cartUid) {
        return $http.get(environment.shoppingCartUrl + '/carts/' + cartUid + '/deliveryoption').then(function(response) {
            return response.data;
        });
    };

    var getSageMerchantKey = function() {
        return $http.get(environment.shoppingCartUrl + '/carts/sage').then(function(response) {
            return response.data;
        });
    };

    var submitTrnsactionToSage = function(cartUid, card) {
        return $http.post(environment.shoppingCartUrl + '/carts/sage/transactions/' + cartUid, card).then(function(response) {
            return response.data;
        });
    };

    var getOrderData = function(cartUid) {
        return $http.get(environment.shoppingCartUrl + '/carts/order-data/' + cartUid).then(function(response) {
            return response.data;
        });
    };

    var getVoucher = function(customerId, status) {
        return $http.get(environment.shoppingCartUrl + '/carts/vouchers?customerId=' + customerId + '&status=' + status).then(function(response) {
           return response.data;
        });
    };

    var applyVoucher = function(cartUid, voucherCode) {
        return $http.post(environment.shoppingCartUrl + '/carts/' + cartUid + '/promotion', voucherCode).then(function(response) {
            return response.data;
        });
    }

    var removeVoucher = function(cartUid) {
        return $http.delete(environment.shoppingCartUrl + '/carts/' + cartUid + '/promotion').then(function(response) {
            return response.data;
        });
    }

    var addWelcomeVoucher = function(customerId, email) {
        var customerData = {id: customerId, email: email};
        return $http.post(environment.shoppingCartUrl + '/carts/vouchers/welcome', customerData).then(function(response) {
            return response.data;
        });
    };

    return {
        addCustomerInfo: addCustomerInfo,
        addItemToShoppingCart: addItemToShoppingCart,
        createShoppingCartForCustomer: createShoppingCartForCustomer,
        createShoppingCartForGuest: createShoppingCartForGuest,
        getShoppingCart: getShoppingCart,
        getShoppingCartByCustomerId: getShoppingCartByCustomerId,
        deleteItemFromShoppingCart: deleteItemFromShoppingCart,
        updateShoppingCartItem: updateShoppingCartItem,
        addAddress: addAddress,
        addDeliveryOption: addDeliveryOption,
        getDeliveryOption: getDeliveryOption,
        getSageMerchantKey: getSageMerchantKey,
        submitTrnsactionToSage: submitTrnsactionToSage,
        getOrderData: getOrderData,
        getVoucher: getVoucher,
        applyVoucher: applyVoucher,
        removeVoucher: removeVoucher,
        addWelcomeVoucher: addWelcomeVoucher
    }
});

cart.config(
    function($routeProvider) {
        $routeProvider.
        when('/cart', {
            templateUrl: 'cart_component/cart.html',
            controller: 'cartCtrl'
        }).when('/cart/:cartUid', {
            templateUrl: 'cart_component/cart.html',
            controller: 'cartCtrl'
        });

    });