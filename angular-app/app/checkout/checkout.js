var checkout = angular.module('checkout', []);

checkout.component('summary', {
    templateUrl: 'checkout/summary.html',
    controller: function($scope, $localstorage, shoppingCartFactory) {
        shoppingCartFactory.getShoppingCart($localstorage.get('cart_uid')).then(function(response) {
            $scope.shoppingCartData = response;
        });
    }
});

checkout.component('progressbar', {
    templateUrl: 'checkout/checkout-progress.html',
    controller: function() {},
    bindings: {
        value: '@'
    }
});

checkout.controller('shippingCtrl', function($scope, $location, $localstorage, $rootScope, customerFactory, shoppingCartFactory) {

    $scope.template.header = 'checkout-header.html';
    $scope.template.footer = 'default-footer.html';
    $scope.saveAddressLoading = false;
    $scope.customerId = $localstorage.get("customer_id");

    customerFactory.getAddressesById($localstorage.get("customer_id")).then(function(response) {
        $scope.addresses = response;
        $scope.selected = 0;
        angular.forEach($scope.addresses, function(address, index) {
            if (address.defaultAddress) {
                $scope.address = address;
            }
        });
    });

    $scope.select = function(index, address) {
        $scope.selected = index;
        $scope.address = address;
    };

    $scope.saveAddress = function(address) {
        $scope.saveAddressLoading = true;
        var cartUid = $localstorage.get('cart_uid');
        var addressData = {
            addressType: 'Shipping',
            name: address.name,
            title: address.title,
            mobile: address.mobile,
            addressLine1: address.addressLine1,
            addressLine2: address.addressLine2,
            addressLine3: address.addressLine3,
            city: address.city,
            country: address.country,
            postcode: address.postcode
        };

        shoppingCartFactory.addAddress(cartUid, addressData).then(function(response) {
            $rootScope.$broadcast('updateCartSummary', false);
            $scope.saveAddressLoading = false;
            $location.path("/delivery");
        });
    }
});

checkout.controller('addAddressCtrl', function($scope) {
    $scope.template.header = 'checkout-header.html';
    $scope.template.footer = 'default-footer.html';
});

checkout.controller('editAddressCtrl', function($scope) {
    $scope.template.header = 'checkout-header.html';
    $scope.template.footer = 'default-footer.html';
});

checkout.controller('guestAddressCtrl', function($scope) {
    $scope.template.header = 'checkout-header.html';
    $scope.template.footer = 'default-footer.html';
});

checkout.controller('billingCtrl', function($scope, $location, $localstorage, $rootScope, customerFactory, shoppingCartFactory) {
    $scope.template.header = 'checkout-header.html';
    $scope.template.footer = 'default-footer.html';
    $scope.saveAddressLoading = false;

    var customerId = $localstorage.get("customer_id");

    customerFactory.getAddressesById(customerId).then(function(response) {
        $scope.addresses = response;
        $scope.selected = 0;
        angular.forEach($scope.addresses, function(address, index) {
            if (address.defaultAddress) {
                $scope.address = address;
            }
        });
    });

    $scope.select = function(index, address) {
        $scope.selected = index;
        $scope.address = address;
    };

    $scope.saveAddress = function(address) {
        $scope.saveAddressLoading = true;
        var cartUid = $localstorage.get('cart_uid');
        var addressData = {
            addressType: 'Billing',
            name: address.name,
            title: address.title,
            mobile: address.mobile,
            addressLine1: address.addressLine1,
            addressLine2: address.addressLine2,
            addressLine3: address.addressLine3,
            city: address.city,
            country: address.country,
            postcode: address.postcode
        };

        shoppingCartFactory.addAddress(cartUid, addressData).then(function(response) {
            $rootScope.$broadcast('updateCartSummary', false);
            $scope.saveAddressLoading = false;
            $location.path("/payment");
        });
    }

});

checkout.controller('deliveryCtrl', function($scope, $location, $localstorage, $rootScope, shoppingCartFactory) {
    $scope.template.header = 'checkout-header.html';
    $scope.template.footer = 'default-footer.html';
    $scope.saveDeliveryOptionLoading = false;

    shoppingCartFactory.getDeliveryOption($localstorage.get('cart_uid')).then(function(response) {
        $scope.deliverOptions = response;
    });

    $scope.saveDeliveryOption = function(deliverOption) {
        $scope.saveDeliveryOptionLoading = true;
        var cartUid = $localstorage.get('cart_uid');
        shoppingCartFactory.addDeliveryOption(cartUid, deliverOption).then(function(response) {
            $rootScope.$broadcast('updateCartSummary', false);
            $scope.saveDeliveryOptionLoading = false;
            if ($localstorage.get('customer_id')) {
                $location.path("/billing");
            } else {
                $location.path("/payment");
            }
        });
    };

    $scope.select = function(index, deliverOption) {
        $scope.selected = index;
        $scope.deliverOption = deliverOption;
    };
});

checkout.controller('paymentCtrl', function($scope, $location, $localstorage, shoppingCartFactory, orderFactory) {
    $scope.template.header = 'checkout-header.html';
    $scope.template.footer = 'default-footer.html';
    $scope.loading = false;

    shoppingCartFactory.getSageMerchantKey().then(function(response) {
        $scope.sageMerchantKey = response.merchantSessionKey;
    });

    var getOrderData = function() {
        var cartUid = $localstorage.get('cart_uid');
        return shoppingCartFactory.getOrderData(cartUid).then(function(orderData) {
            return orderData;
        }, function(error) {
            return $q.reject("Failed to retrieve order data");
        });
    };

    var createOrder = function(orderData) {
        return orderFactory.createOrder(orderData).then(function(orderNumber) {
            $scope.orderNumber = orderNumber;
            return orderNumber;
        }, function(error) {
            return $q.reject("Failed to create order");
        });
    };

    var submitTransactionToSage = function() {
        return shoppingCartFactory.submitTrnsactionToSage($localstorage.get('cart_uid'), $scope.card).then(function(response) {
            return response;
        }, function(error) {
            return $q.reject("Failed to submit order");
        });
    };

    var markOrderAsPaid = function() {
        var orderStatus = {
            status: 'Processing',
            description: 'Order paid'
        };
        return orderFactory.addOrderStatus($scope.orderNumber, orderStatus).then(function(order) {
            return order;
        }, function(error) {
            return $q.reject("Failed to update order status");
        });
    };

    var redirectToOrderConfirmationPage = function() {
        $localstorage.remove('cart_uid');
        var customerId = $localstorage.get('customer_id');
        $location.path("/order-confirmation/" + $scope.orderNumber);
    };

    var failed = function(error) {
        $scope.loading = false;
        console.log(error);
        console.log('failed to submit order');
    };

    $scope.select = function(option) {
        $scope.option = option;
    };

    $scope.submitOrder = function() {
        $scope.loading = true;
        sagepayOwnForm({
                merchantSessionKey: $scope.sageMerchantKey
            })
            .tokeniseCardDetails({
                cardDetails: {
                    cardholderName: $scope.card.name,
                    cardNumber: $scope.card.number,
                    expiryDate: $scope.card.expiryDate,
                    securityCode: $scope.card.securityCode
                },
                onTokenised: function(result) {
                    if (result.success) {

                        $scope.card = {
                            merchantSessionKey: $scope.sageMerchantKey,
                            cardIdentifier: result.cardIdentifier
                        };

                        getOrderData()
                            .then(createOrder)
                            .then(submitTransactionToSage)
                            .then(markOrderAsPaid)
                            .then(redirectToOrderConfirmationPage, failed);
                    } else {
                        alert(JSON.stringify(result));
                    }
                }
            });
    };
});

checkout.controller('stripePaymentCtrl', function($scope, $location, $localstorage, shoppingCartFactory, orderFactory) {
    $scope.template.header = 'checkout-header.html';
    $scope.template.footer = 'default-footer.html';
    shoppingCartFactory.getOrderData($localstorage.get('cart_uid')).then(function(response) {
        $scope.orderData = response;

        var stripe = Stripe('pk_test_ZCtSfzQis8KdwNXWTNsJsq1W00qzNYAJRM');
        var elements = stripe.elements();

        var style = {
            base: {
                color: "#32325d",
                fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
                fontSmoothing: 'antialiased',
                fontSize: "18px",
                "::placeholder": {
                    color: "#aab7c4"
                }
            },
            invalid: {
                color: "#D9534F",
                iconColor: "#D9534F"
            }
        };

        var card = elements.create("card", {
            style: style
        });
        card.mount("#card-element");

        card.addEventListener('change', function(event) {
            var displayError = document.getElementById('card-errors');
            if (event.error) {
                displayError.textContent = event.error.message;
            } else {
                displayError.textContent = '';
            }
        });

        var form = document.getElementById('payment-form');

        form.addEventListener('submit', function(event) {
            event.preventDefault();
            $scope.showSpinner();
            card.update({disabled: true});

            orderFactory.createOrder($scope.orderData).then(function(orderNumber) {
                $scope.orderNumber = orderNumber;
                orderFactory.downloadStripeClientSecret($scope.orderNumber, $localstorage.get('cart_uid')).then(function(response) {
                    $scope.clientSecret = response;
                    stripe.confirmCardPayment($scope.clientSecret, {
                        payment_method: {
                            card: card
                        }
                    }).then(function(result) {
                        if (result.error) {
                            var displayError = document.getElementById('card-errors');
                            displayError.textContent = result.error.message;
                            $scope.hideSpinner();
                            card.update({disabled: false});
                        } else {
                            // The payment has been processed!
                            if (result.paymentIntent.status === 'succeeded') {
                                $scope.hideSpinner();
                                card.update({disabled: false});
                                $localstorage.remove('cart_uid');
                                $scope.$apply(function() {
                                    $location.path("/order-confirmation/" + $scope.orderNumber);
                                });
                            }
                        }
                    });
                }, function(error) {
                    var displayError = document.getElementById('card-errors');
                    displayError.textContent = 'Failed to initialise payment. Please try again later.';
                    $scope.hideSpinner();
                    card.update({disabled: false});
                });
            }, function(error) {
                var displayError = document.getElementById('card-errors');
                displayError.textContent = 'Failed to create order. Your card hasn\'t been charged, please try again later.';
                $scope.hideSpinner();
                card.update({disabled: false});
            });

        });
    });

    $scope.showSpinner = function() {
        document.getElementById('btnSpinner').classList.add('glyphicon', 'glyphicon-refresh', 'spinning');
    }

    $scope.hideSpinner = function() {
        document.getElementById('btnSpinner').classList.remove('glyphicon', 'glyphicon-refresh', 'spinning');
    }
});

checkout.controller('orderConfirmationCtrl', function($scope, $location, $localstorage, $routeParams, shoppingCartFactory, orderFactory) {
    $scope.template.header = 'cart-header.html';
    $scope.template.footer = 'default-footer.html';

    if ($localstorage.get('current_user')) {
        $scope.user = $localstorage.get('current_user');
    } else {
        $scope.user = 'Guest'
    }

    orderFactory.getOrderByNumber($routeParams.orderNumber).then(function(response) {
        $scope.order = response;
    });
});

checkout.config(
    function($routeProvider) {
        $routeProvider
            .when('/shipping', {
                templateUrl: 'checkout/shipping.html',
                controller: 'shippingCtrl'
            })
            .when('/delivery', {
                templateUrl: 'checkout/delivery.html',
                controller: 'deliveryCtrl'
            })
            .when('/billing', {
                templateUrl: 'checkout/billing.html',
                controller: 'billingCtrl'
            })
            .when('/payment-sage', {
                templateUrl: 'checkout/payment.html',
                controller: 'paymentCtrl'
            })
            .when('/payment', {
                templateUrl: 'checkout/stripe-payment.html',
                controller: 'stripePaymentCtrl'
            })
            .when('/order-confirmation/:orderNumber', {
                templateUrl: 'checkout/order-confirmation.html',
                controller: 'orderConfirmationCtrl'
            })
            .when('/checkout/shipping/address/add', {
                templateUrl: 'checkout/add-shipping-address.html',
                controller: 'addAddressCtrl'
            })
            .when('/checkout/shipping/:id/address/:addressId/edit', {
                templateUrl: 'checkout/edit-shipping-address.html',
                controller: 'editAddressCtrl'
            })
            .when('/checkout/billing/address/add', {
                templateUrl: 'checkout/add-billing-address.html',
                controller: 'addAddressCtrl'
            })
            .when('/checkout/billing/:id/address/:addressId/edit', {
                templateUrl: 'checkout/edit-billing-address.html',
                controller: 'editAddressCtrl'
            })
            .when('/checkout/guest/address', {
                templateUrl: 'checkout/add-guest-address.html',
                controller: 'guestAddressCtrl'
            })
            .when('/checkout/guest/delivery', {
                templateUrl: 'checkout/delivery.html',
                controller: 'deliveryCtrl'
            });
    });