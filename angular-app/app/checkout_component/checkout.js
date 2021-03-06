var checkout = angular.module('checkout', ['ngSanitize']);

checkout.component('summary', {
    templateUrl: 'checkout_component/summary.html',
    controller: function($scope, $localstorage, shoppingCartFactory) {
        shoppingCartFactory.getShoppingCart($localstorage.get('cart_uid')).then(function(response) {
            $scope.shoppingCartData = response;
        });

        if($localstorage.containsKey("customer_id")){
            shoppingCartFactory.getVoucher($localstorage.get("customer_id"), 'active').then(function(response) {
                $scope.vouchers = response;
            });
        }

        $scope.applying = false;
        $scope.removing = false;
        $scope.hasError = false;
        $scope.code = '';
        $scope.showVouchers = false;
        $scope.vouchers = [];

        $scope.apply = function(){
            $scope.hasError = false;
            $scope.applying = true;
            if (!$localstorage.containsKey('cart_uid') || $scope.code == '') {
                $scope.hasError = true;
                $scope.errorMsg = 'Something went wrong, please try again.';
                $scope.applying = false;
                $scope.code = '';
            } else {
                shoppingCartFactory.applyVoucher($localstorage.get('cart_uid'), $scope.code).then(function(response) {
                    $scope.shoppingCartData = response;
                    $scope.applying = false;
                    $scope.code = '';
                }, function(error){
                    $scope.hasError = true;
                    $scope.errorMsg = error.data.errorMsg;
                    $scope.applying = false;
                    $scope.code = '';
                });
            }
        }

        $scope.removeVoucher = function(){
            $scope.removing = true;
            shoppingCartFactory.removeVoucher($localstorage.get('cart_uid')).then(function(response) {
                $scope.shoppingCartData = response;
                $scope.removing = false;
            });
        }
    },
    bindings: {novoucher: '@'}
});

checkout.component('progressbar', {
    templateUrl: 'checkout_component/checkout-progress.html',
    controller: function() {},
    bindings: {
        value: '@'
    }
});

checkout.controller('shippingCtrl', function($scope, $location, $localstorage, $rootScope, customerFactory, shoppingCartFactory) {

    $scope.template.header = 'checkout-shipping-header.html';
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
            $location.path("/checkout/delivery");
        });
    }
});

checkout.controller('addShippingAddressCtrl', function($scope) {
    $scope.template.header = 'checkout-shipping-header.html';
    $scope.template.footer = 'default-footer.html';
});

checkout.controller('editShippingAddressCtrl', function($scope) {
    $scope.template.header = 'checkout-shipping-header.html';
    $scope.template.footer = 'default-footer.html';
});

checkout.controller('addBillingAddressCtrl', function($scope) {
    $scope.template.header = 'checkout-billing-header.html';
    $scope.template.footer = 'default-footer.html';
});

checkout.controller('editBillingAddressCtrl', function($scope) {
    $scope.template.header = 'checkout-billing-header.html';
    $scope.template.footer = 'default-footer.html';
});

checkout.controller('guestAddressCtrl', function($scope, shoppingCartFactory, $localstorage, $rootScope, $location) {
    $scope.template.header = 'checkout-shipping-header.html';
    $scope.template.footer = 'default-footer.html';

    $scope.billToSameAddress = true;
    $scope.shippingFormInvalid = false;
    $scope.billingFormInvalid = false;
    $scope.submitting = false;
    
    shoppingCartFactory.getShoppingCart($localstorage.get('cart_uid')).then(function(response){
        $scope.shoppingCart = response;
        if($scope.shoppingCart.shipping != null){
            $scope.shipping = $scope.shoppingCart.shipping;
            $scope.shipping.addressType = 'Shipping';
        } else {
            $scope.shipping = {
                addressType: 'Shipping',
                country: 'United Kingdom'
            };
        }

        if($scope.shoppingCart.billing != null){
            $scope.billing = $scope.shoppingCart.billing;
            $scope.billing.addressType = 'Billing';
        } else {
            $scope.billing = {
                addressType: 'Billing',
                country: 'United Kingdom'
            };
        }    
    });

    shoppingCartFactory.getDeliveryOption($localstorage.get('cart_uid')).then(function(response) {
        $scope.deliveryOptions = response;
        $scope.deliveryOption = $scope.deliveryOptions[0];
    });

    $scope.toggle = function(){
        if (!$scope.billToSameAddress) {
            $scope.billing.title = $scope.shipping.title;
            $scope.billing.name = $scope.shipping.name;
            $scope.billing.addressLine1 = $scope.shipping.addressLine1;
            $scope.billing.addressLine2 = $scope.shipping.addressLine2;
            $scope.billing.city = $scope.shipping.city;
            $scope.billing.postcode = $scope.shipping.postcode;
            $scope.billing.mobile = $scope.shipping.mobile;
        }
    }

    $scope.select = function(deliveryOption){
        $scope.deliveryOption = deliveryOption;
    }

    $scope.submit = function(){
        $scope.submitting = true;

        if ($scope.billToSameAddress) {
            $scope.shippingFormInvalid = $scope.shippingForm.$invalid;
            if($scope.shippingFormInvalid){
                $scope.submitting = false;
                return;
            }
            $scope.billing.title = $scope.shipping.title;
            $scope.billing.name = $scope.shipping.name;
            $scope.billing.addressLine1 = $scope.shipping.addressLine1;
            $scope.billing.addressLine2 = $scope.shipping.addressLine2;
            $scope.billing.city = $scope.shipping.city;
            $scope.billing.postcode = $scope.shipping.postcode;
            $scope.billing.mobile = $scope.shipping.mobile;
        } else {
            $scope.shippingFormInvalid = $scope.shippingForm.$invalid;
            $scope.billingFormInvalid = $scope.billingForm.$invalid;

            if($scope.shippingFormInvalid || $scope.billingFormInvalid){
                $scope.submitting = false;
                return;
            }
        }

        shoppingCartFactory.addAddress($localstorage.get('cart_uid'), $scope.shipping).then(function(response) {
            shoppingCartFactory.addAddress($localstorage.get('cart_uid'), $scope.billing).then(function(response) {
                shoppingCartFactory.addDeliveryOption($localstorage.get('cart_uid'), $scope.deliveryOption).then(function(response) {
                    $rootScope.$broadcast('updateCartSummary', false);
                    $scope.submitting = false;
                    $location.path("/checkout/payment");
                });
            });
        });
    }
});

checkout.controller('billingCtrl', function($scope, $location, $localstorage, $rootScope, customerFactory, shoppingCartFactory) {
    $scope.template.header = 'checkout-billing-header.html';
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
            $location.path("/checkout/payment");
        });
    }

});

checkout.controller('deliveryCtrl', function($scope, $location, $localstorage, $rootScope, shoppingCartFactory) {
    $scope.template.header = 'checkout-delivery-header.html';
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
                $location.path("/checkout/billing");
            } else {
                $location.path("/checkout/payment");
            }
        });
    };

    $scope.select = function(index, deliverOption) {
        $scope.selected = index;
        $scope.deliverOption = deliverOption;
    };
});

checkout.controller('stripePaymentCtrl', function($scope, $location, $localstorage, shoppingCartFactory, orderFactory, $rootScope, environment) {

    if($localstorage.containsKey("customer_id")){
        $scope.template.header = 'checkout-payment-header.html';
        $scope.template.footer = 'default-footer.html';
    } else {
        $scope.template.header = 'guest-checkout-payment-header.html';
        $scope.template.footer = 'default-footer.html';
    }

    $scope.paymentOption = 'card';

    shoppingCartFactory.getOrderData($localstorage.get('cart_uid')).then(function(response) {
        $scope.orderData = response;

        var stripe = Stripe(environment.stripePublishableKey);
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

                var stripeMetaData = {
                    shoppingCartId : $localstorage.get('cart_uid'),
                    userName : $localstorage.get('current_user'),
                    siteName : environment.siteName,
                    homePage : environment.homePage,
                    registrationPage : environment.registrationPage
                };

                orderFactory.downloadStripeClientSecret($scope.orderNumber, stripeMetaData).then(function(response) {
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
                                $rootScope.$broadcast('resetCartInfo');
                                $scope.$apply(function() {
                                    $location.path("/checkout/" + $scope.orderNumber + "/thank_you");
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

        var paymentRequest = stripe.paymentRequest({
          country: 'GB',
          currency: 'gbp',
          total: {
            label: 'Order total',
            amount: Math.round(($scope.orderData.orderTotal * 100) * 1e12) / 1e12,
          },
          requestPayerName: true,
          requestPayerEmail: true,
        });

        var prButton = elements.create('paymentRequestButton', {
          paymentRequest: paymentRequest,
        });

        paymentRequest.canMakePayment().then(function(result) {
          if (result) {
            prButton.mount('#payment-request-button');
          } else {
            document.getElementById('payment-request-button').style.display = 'none';
            var displayError = document.getElementById('wallet-payment-errors');
            displayError.textContent = "I am sorry, it seems that your device/browser combination does not support Google Pay or Apply Pay."
          }
        });

        paymentRequest.on('paymentmethod', function(ev) {
            orderFactory.createOrder($scope.orderData).then(function(orderNumber) {
                $scope.orderNumber = orderNumber;

                var stripeMetaData = {
                    shoppingCartId: $localstorage.get('cart_uid'),
                    userName: $localstorage.get('current_user'),
                    siteName: environment.siteName,
                    homePage: environment.homePage,
                    registrationPage: environment.registrationPage
                };

                orderFactory.downloadStripeClientSecret($scope.orderNumber, stripeMetaData).then(function(response) {
                    $scope.clientSecret = response;

                    // Confirm the PaymentIntent without handling potential next actions (yet).
                    stripe.confirmCardPayment($scope.clientSecret, {payment_method: ev.paymentMethod.id}, {handleActions: false}).then(function(confirmResult) {
                        if (confirmResult.error) {
                            var displayError = document.getElementById('wallet-payment-errors');
                            displayError.textContent = confirmResult.error;
                            ev.complete('fail');
                        } else {
                            // Report to the browser that the confirmation was successful, prompting
                            // it to close the browser payment method collection interface.
                            ev.complete('success');
                            // Check if the PaymentIntent requires any actions and if so let Stripe.js
                            // handle the flow. If using an API version older than "2019-02-11"
                            // instead check for: `paymentIntent.status === "requires_source_action"`.
                            if (confirmResult.paymentIntent.status === "requires_action") {
                                // Let Stripe.js handle the rest of the payment flow.
                                stripe.confirmCardPayment($scope.clientSecret).then(function(result) {
                                    if (result.error) {
                                        var displayError = document.getElementById('wallet-payment-errors');
                                        displayError.textContent = result.error;
                                    } else {
                                        $rootScope.$broadcast('resetCartInfo');
                                        $scope.$apply(function() {
                                            $location.path("/checkout/" + $scope.orderNumber + "/thank_you");
                                        });
                                    }
                                });
                            } else {
                                $rootScope.$broadcast('resetCartInfo');
                                $scope.$apply(function() {
                                    $location.path("/checkout/" + $scope.orderNumber + "/thank_you");
                                });
                            }
                        }
                    });
                }, function(error) {
                    var displayError = document.getElementById('wallet-payment-errors');
                    displayError.textContent = 'Failed to initialise payment. Please try again later.';
                });
            }, function(error) {
                var displayError = document.getElementById('wallet-payment-errors');
                displayError.textContent = 'Failed to create order. Your card hasn\'t been charged, please try again later.';
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

checkout.controller('orderConfirmationCtrl', function($scope, $localstorage, $routeParams, orderFactory, environment) {
    $scope.template.header = 'cart-header.html';
    $scope.template.footer = 'default-footer.html';

    if($localstorage.containsKey("customer_id")){
        $scope.message = 'You can view the status of your order by visiting Your Orders on <a href="'+environment.homePage+'">'+environment.siteName+'</a>';
    } else {
        $scope.message = 'Would you like to create an account with us to track your order on '+environment.siteName+'? <a href="' + environment.registrationPage + $routeParams.orderNumber + '">Click here to create account</a>';
    }

    orderFactory.getOrderByNumber($routeParams.orderNumber).then(function(response) {
        $scope.order = response;
    });
});

checkout.config(
    function($routeProvider) {
        $routeProvider
            .when('/checkout/shipping', {
                templateUrl: 'checkout_component/shipping.html',
                controller: 'shippingCtrl'
            })
            .when('/checkout/delivery', {
                templateUrl: 'checkout_component/delivery.html',
                controller: 'deliveryCtrl'
            })
            .when('/checkout/billing', {
                templateUrl: 'checkout_component/billing.html',
                controller: 'billingCtrl'
            })
            .when('/checkout/payment', {
                templateUrl: 'checkout_component/stripe-payment.html',
                controller: 'stripePaymentCtrl'
            })
            .when('/checkout/shipping/address/add', {
                templateUrl: 'checkout_component/add-shipping-address.html',
                controller: 'addShippingAddressCtrl'
            })
            .when('/checkout/shipping/:id/address/:addressId/edit', {
                templateUrl: 'checkout_component/edit-shipping-address.html',
                controller: 'editShippingAddressCtrl'
            })
            .when('/checkout/billing/address/add', {
                templateUrl: 'checkout_component/add-billing-address.html',
                controller: 'addBillingAddressCtrl'
            })
            .when('/checkout/billing/:id/address/:addressId/edit', {
                templateUrl: 'checkout_component/edit-billing-address.html',
                controller: 'editBillingAddressCtrl'
            })
            .when('/checkout/guest', {
                templateUrl: 'checkout_component/guest-checkout.html',
                controller: 'guestAddressCtrl'
            })
            .when('/checkout/guest/payment', {
                templateUrl: 'checkout_component/stripe-payment.html',
                controller: 'stripePaymentCtrl'
            })
            .when('/checkout/:orderNumber/thank_you', {
                templateUrl: 'checkout_component/thank-you.html',
                controller: 'orderConfirmationCtrl'
            });
    });