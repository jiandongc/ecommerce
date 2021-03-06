var customer = angular.module('customer', ['ngRoute', 'ngResource']);

customer.component('address', {
    templateUrl: 'component/new-address-form.html',
    controller: function($scope, $rootScope, $localstorage, $location, $window, customerFactory, shoppingCartFactory) {
        $scope.saving = false;
        $scope.address = {};

        if ($localstorage.get('customer_id')) {
            customerFactory.getCustomerById($localstorage.get('customer_id')).then(function(response) {
                $scope.address.title = response.title;
                $scope.address.name = response.name;
                $scope.address.mobile = response.mobile;
                $scope.address.defaultAddress = true;
                $scope.address.country = 'United Kingdom';
                $scope.customer = response;
            });
        } else {
            $scope.address.defaultAddress = true;
            $scope.address.country = 'United Kingdom';
        }

        $scope.addAddress = function(address) {
            $scope.saving = true;
            if ($localstorage.get('customer_id')) {
                customerFactory.addAddress($localstorage.get('customer_id'), address).then(function(data) {
                    $scope.error = false;
                    $scope.saving = false;
                    $window.history.back();
                }, function(error) {
                    $scope.error = true;
                    $scope.saving = false;
                    $scope.errorMsg = error;
                })
            } else {
                var shippingAddressData = {
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

                var billingAddressData = {
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

                shoppingCartFactory.addAddress($localstorage.get('cart_uid'), shippingAddressData).then(function(response) {
                    $rootScope.$broadcast('updateCartSummary', false);
                    shoppingCartFactory.addAddress($localstorage.get('cart_uid'), billingAddressData).then(function(response) {
                        $rootScope.$broadcast('updateCartSummary', false);
                        $scope.saving = false;
                        $location.path("/checkout/guest/delivery");
                    });
                });
            }
        };
    },
    bindings: {
        cancelurl: '@',
        guest: '@'
    }
});

customer.component('editaddress', {
    templateUrl: 'component/edit-address-form.html',
    controller: function($scope, $routeParams, $localstorage, $window, customerFactory) {
        $scope.saving = false;

        customerFactory.getAddressById($routeParams.id, $routeParams.addressId).then(function(response) {
            $scope.address = response;
        });

        $scope.updateAddress = function(address) {
            $scope.saving = true;
            customerFactory.updateAddress($localstorage.get('customer_id'), address).then(function(data) {
                $scope.error = false;
                $scope.saving = false;
                $window.history.back();
            }, function(error) {
                $scope.error = true;
                $scope.saving = false;
                $scope.errorMsg = error;
            })
        }
    },
    bindings: {
        cancelurl: '@'
    }
});

customer.controller('loginCtrl', function($scope, authService, $rootScope, $localstorage, $location, shoppingCartFactory) {

    $rootScope.loginError = false;
    $rootScope.logining = false;
    $scope.guestLoading = false;

    $scope.login = function(credentials) {
        $rootScope.logining = true;
        authService.authenticateUser(credentials);
    };

    $scope.guestContinue = function(guest) {
        $rootScope.guestLoading = true;
        shoppingCartFactory.addCustomerInfo($localstorage.get('cart_uid'), guest).then(function(data) {
            $scope.guestLoading = false;
            $location.path("/checkout/guest");
        }, function(error) {
            $scope.guestLoading = false;
        });

    }
});

customer.controller('registerCtrl', function($scope, $routeParams, $localstorage, authService, customerFactory, orderFactory, authFactory, shoppingCartFactory) {

    $scope.error = false;
    $scope.loading = false;
    $scope.errorMsg = "";

    $scope.registerCustomer = function() {
        $scope.loading = true;
        var newCustomer = {
            name: $scope.customer.name,
            title: $scope.customer.title,
            email: $scope.customer.email,
            mobile: $scope.customer.mobile,
            password: $scope.customer.password
        };

        customerFactory.save(newCustomer).then(function(data) {

            var credentials = {
                email: data.email,
                password: newCustomer.password
            };

            authFactory.validateUser(credentials).then(function(response) {
                $localstorage.set('access_token', response.headers("Authentication"));
                orderFactory.addCustomerInfo(data.email, data.id);
                shoppingCartFactory.addWelcomeVoucher(data.id, data.email);
                authService.authenticateUser(credentials);
            });

            $scope.error = false;
            $scope.loading = false;
        }, function(error) {
            $scope.error = true;
            $scope.loading = false;
            if (error.status === 409) {
                $scope.errorMsg = "This email address is already used.";
            } else {
                $scope.errorMsg = "Technical error, please contact site admin: info@noodle-monster.co.uk";
            }
        });
    };
});

customer.controller('accountCtrl', function($scope, $routeParams, $rootScope, $location, customerFactory) {
    $scope.loading = true;
    customerFactory.getCustomerById($routeParams.id).then(function(response) {
        $scope.customer = response;
        $scope.loading = false;
    });

    $scope.logout = function() {
        $rootScope.$broadcast('resetUserInfo');
        $rootScope.$broadcast('resetCartInfo');
        $location.path("#");
    }
});

customer.controller('orderCtrl', function($scope, $routeParams, orderFactory, customerFactory) {
    $scope.status = $routeParams.status;
    if ($scope.status !== 'open') {
        $scope.status = 'completed'
    }

    customerFactory.getCustomerById($routeParams.id).then(function(response) {
        $scope.customer = response;
        orderFactory.getOrderByCustomerId($routeParams.id, $scope.status).then(function(response) {
            $scope.orders = response;
        });
    });
});

customer.controller('orderDetailsCtrl', function($scope, $routeParams, orderFactory, customerFactory) {
    customerFactory.getCustomerById($routeParams.id).then(function(response) {
        $scope.customer = response;
        orderFactory.getOrderByNumber($routeParams.orderNumber).then(function(response) {
            $scope.order = response;
        });
    });
});

customer.controller('profileEditCtrl', function($scope, $routeParams, $location, $localstorage, customerFactory) {
    $scope.saving = false;
    customerFactory.getCustomerById($routeParams.id).then(function(response) {
        $scope.customer = response;
    });

    $scope.update = function(customer) {
        $scope.saving = true;
        customerFactory.update(customer).then(function(data) {
            $scope.error = false;
            $scope.saving = false;
            $localstorage.set('current_user', data.name);
            $localstorage.set('customer_id', data.id);
            $location.path("/account/" + data.id + "/profile");
        }, function(error) {
            $scope.error = true;
            $scope.saving = false;
            if (error.status === 409) {
                $scope.errorMsg = "This the email address is already used.";
            } else {
                $scope.errorMsg = "Technical error, please contact site admin: info@noodle-monster.co.uk";
            }
        });
    };

    $scope.cancel = function() {
        $location.path("/account/" + $scope.customer.id + "/profile");
    }
});

customer.controller('passwordUpdateCtrl', function($scope, $routeParams, $location, $localstorage, customerFactory) {
    $scope.updating = false;
    customerFactory.getCustomerById($routeParams.id).then(function(response) {
        $scope.customer = response;
    });

    $scope.update = function(password) {
        $scope.updating = true;
        customerFactory.updatePassword($scope.customer.id, password).then(function(data) {
            $scope.error = false;
            $localstorage.set('current_user', data.name);
            $localstorage.set('customer_id', data.id);
            $location.path("/account/" + data.id + "/profile");
        }, function(error) {
            $scope.error = true;
            $scope.updating = false;
            if (error.status === 409) {
                $scope.errorMsg = "This the email address is already used.";
            } else {
                $scope.errorMsg = "Technical error, please contact site admin: info@noodle-monster.co.uk";
            }
        });
    };

    $scope.cancel = function() {
        $location.path("/account/" + $scope.customer.id + "/profile");
    }
});

customer.controller('addressBookCtrl', function($scope, $routeParams, $route, customerFactory) {
    customerFactory.getAddressesById($routeParams.id).then(function(response) {
        $scope.customer = {
            id: $routeParams.id
        };
        $scope.addresses = response;
    });

    $scope.setAsDefault = function(address) {
        address.defaultAddress = true;
        customerFactory.updateAddress($scope.customer.id, address).then(function(data) {
            $scope.error = false;
            $route.reload();
        }, function(error) {
            $scope.error = true;
            $scope.errorMsg = error;
        })
    };

    $scope.removeAddress = function(address) {
        customerFactory.removeAddress($scope.customer.id, address).then(function(data) {
            $scope.error = false;
            $route.reload();
        }, function(error) {
            $scope.error = true;
            $scope.errorMsg = error;
        })
    }
});

customer.controller('editAddressCtrl', function($scope, $localstorage) {
    $scope.customerId = $localstorage.get('customer_id');
});

customer.controller('addAddressCtrl', function($scope, $localstorage) {
    $scope.customerId = $localstorage.get('customer_id');
});

customer.controller('favouriteCtrl', function($scope, $localstorage, $q, $route, customerFactory, productFactory) {
    $scope.loading = true;
    $scope.products = [];
    customerFactory.getFavouriteItems($localstorage.get('customer_id')).then(function(response) {
        var promises = [];
        for (i = 0; i < response.length; i = i + 1) {
            promises.push(
                productFactory.getProductWithCode(response[i].productCode).then(function(response){
                    return response;
                }, function(error){
                }
            ));
        }

        $q.all(promises).then(function(response) {
            for (var i = 0; i < response.length; i++) {

                if (typeof response[i] === 'undefined') {
                    continue;
                }

                var product = {
                    code: response[i].code,
                    images: response[i].images,
                    name: response[i].name,
                    description: response[i].description,
                    price: response[i].price,
                    removing: false
                };

                $scope.products.push(product);
            }
            $scope.loading = false;
        })
    });

    $scope.removeFavouriteItem = function(product, index) {
        $scope.products[index].removing = true;
        customerFactory.removeFavouriteItem($localstorage.get('customer_id'), product).then(function(data) {
            $route.reload();
        }, function(error) {
            $scope.products[index].removing = false;
        })
    }
});

customer.controller('forgetPasswordCtrl', function($scope, customerFactory, $location) {
    $scope.submitting = false;
    $scope.submit = function(email) {
        $scope.submitting = true;
        customerFactory.requestPasswordResetToken(email).then(function(data) {
            $scope.submitting = false;
            $location.path("/login/forgotten/confirmation");
        });
    }
});

customer.controller('resetPasswordCtrl', function($scope, customerFactory, $routeParams, $location) {
    $scope.validating = true;
    customerFactory.retrieveToken($routeParams.token).then(function(data) {
        if (data) {
            $scope.token = data.text;
            $scope.validating = false;
        } else {
            $location.path("/login/password/token-expired");
        }
    });

    $scope.submit = function(password, token) {
        $scope.submitting = true;
        customerFactory.resetPassword(password, token).then(function(data) {
            $scope.submitting = false;
            $location.path("/login/password/reset-confirmation");
        });
    }
});

customer.controller('voucherCtrl', function($scope, $routeParams, customerFactory, shoppingCartFactory) {
    customerFactory.getCustomerById($routeParams.id).then(function(response) {
        $scope.customer = response;
        $scope.status = $routeParams.status;
        shoppingCartFactory.getVoucher($routeParams.id, $routeParams.status).then(function(response) {
            $scope.vouchers = response;
        });
    });
});

customer.factory('customerFactory', function($http, environment) {

    var getCustomerByEmail = function(credentials) {
        return $http.get(environment.customerUrl + '/customers?email=' + credentials.email).then(function(response) {
            return response.data;
        });
    };

    var getCustomerById = function(id) {
        return $http.get(environment.customerUrl + '/customers/' + id).then(function(response) {
            return response.data;
        });
    };

    var save = function(customer) {
        var configs = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        return $http.post(environment.customerUrl + '/customers', customer, configs).then(function(response) {
            return response.data;
        });
    };

    var update = function(customer) {
        var configs = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        return $http.put(environment.customerUrl + '/customers', customer, configs).then(function(response) {
            return response.data;
        });
    };

    var getAddressesById = function(customerId) {
        return $http.get(environment.customerUrl + '/customers/' + customerId + '/addresses').then(function(response) {
            return response.data;
        });
    };

    var getAddressById = function(customerId, addressId) {
        return $http.get(environment.customerUrl + '/customers/' + customerId + '/addresses/' + addressId).then(function(response) {
            return response.data;
        });
    };

    var addAddress = function(customerId, address) {
        var configs = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        return $http.post(environment.customerUrl + '/customers/' + customerId + '/addresses', address, configs).then(function(response) {
            return response.data;
        });
    };

    var updateAddress = function(customerId, address) {
        var configs = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        return $http.put(environment.customerUrl + '/customers/' + customerId + '/addresses/' + address.id, address, configs).then(function(response) {
            return response.data;
        });
    };

    var removeAddress = function(customerId, address) {
        return $http.delete(environment.customerUrl + '/customers/' + customerId + '/addresses/' + address.id).then(function(response) {
            return response.data;
        });
    };

    var addToFavourite = function(customerId, productCode) {
        var configs = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        var favouriteProduct = {
            productCode: productCode,
            type: 'FAVOURITE'
        };

        return $http.post(environment.customerUrl + '/customers/' + customerId + '/products/', favouriteProduct, configs).then(function(response) {
            return response.data;
        });
    };

    var getFavouriteItems = function(customerId) {
        return $http.get(environment.customerUrl + '/customers/' + customerId + '/products?type=favourite').then(function(response) {
            return response.data;
        });
    };

    var removeFavouriteItem = function(customerId, product) {
        return $http.delete(environment.customerUrl + '/customers/' + customerId + '/products/?type=favourite&code=' + product.code).then(function(response) {
            return response.data;
        });
    };

    var addToNotifyInStock = function(productCode, email) {
        var configs = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        var data = {
            productCode: productCode,
            type: 'NOTIFY_IN_STOCK',
            email: email
        };

        return $http.post(environment.customerUrl + '/customers/products/', data, configs).then(function(response) {
            return response.data;
        });
    };

    var requestPasswordResetToken = function(email) {
        var configs = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        var passwordResetRequest = {
            email: email,
            type: 'PASSWORD_RESET'
        };

        return $http.post(environment.customerUrl + '/customers/tokens', passwordResetRequest, configs).then(function(response) {
            return response.data;
        });
    };

    var retrieveToken = function(token) {
        return $http.get(environment.customerUrl + '/customers/tokens/' + token).then(function(response) {
            return response.data;
        });
    };

    var resetPassword = function(password, token) {
        var configs = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        var passwordResetRequest = {
            token: token,
            password: password
        };

        return $http.post(environment.customerUrl + '/customers/reset-password', passwordResetRequest, configs).then(function(response) {
            return response.data;
        });
    };

    var updatePassword = function(customerId, password) {
        var configs = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        return $http.put(environment.customerUrl + '/customers/' + customerId + '/password', password, configs).then(function(response) {
            return response.data;
        });
    };

    return {
        getCustomerByEmail: getCustomerByEmail,
        getCustomerById: getCustomerById,
        save: save,
        update: update,
        getAddressesById: getAddressesById,
        getAddressById: getAddressById,
        addAddress: addAddress,
        updateAddress: updateAddress,
        removeAddress: removeAddress,
        addToFavourite: addToFavourite,
        getFavouriteItems: getFavouriteItems,
        removeFavouriteItem: removeFavouriteItem,
        addToNotifyInStock: addToNotifyInStock,
        requestPasswordResetToken: requestPasswordResetToken,
        retrieveToken: retrieveToken,
        resetPassword: resetPassword,
        updatePassword: updatePassword
    };
});

customer.directive('passwordMatch', [function() {
    return {
        restrict: 'A',
        scope: true,
        require: 'ngModel',
        link: function(scope, elem, attrs, control) {
            var checker = function() {
                var e1 = scope.$eval(attrs.ngModel);
                var e2 = scope.$eval(attrs.passwordMatch);
                return e1 == e2;
            };
            scope.$watch(checker, function(n) {
                control.$setValidity("unique", n);
            });
        }
    };
}]);

customer.config(
    function($routeProvider) {
        $routeProvider.
        when('/login', {
            templateUrl: 'customer_component/login.html',
            controller: 'loginCtrl'
        }).when('/login/forgotten', {
            templateUrl: 'customer_component/forgotten-password.html',
            controller: 'forgetPasswordCtrl'
        }).when('/login/forgotten/confirmation', {
            templateUrl: 'customer_component/forgotten-password-confirmation.html'
        }).when('/login/password', {
            templateUrl: 'customer_component/reset-password.html',
            controller: 'resetPasswordCtrl'
        }).when('/login/password/token-expired', {
            templateUrl: 'customer_component/link-expired.html'
        }).when('/login/password/reset-confirmation', {
            templateUrl: 'customer_component/reset-password-confirmation.html'
        }).when('/register/:orderNumber?', {
            templateUrl: 'customer_component/register.html',
            controller: 'registerCtrl'
        }).when('/account/:id', {
            templateUrl: 'customer_component/account.html',
            controller: 'accountCtrl'
        }).when('/account/:id/profile', {
            templateUrl: 'customer_component/profile.html',
            controller: 'accountCtrl'
        }).when('/account/:id/profile-edit', {
            templateUrl: 'customer_component/profile-edit.html',
            controller: 'profileEditCtrl'
        }).when('/account/:id/password-update', {
            templateUrl: 'customer_component/password-update.html',
            controller: 'passwordUpdateCtrl'
        }).when('/account/:id/address-book', {
            templateUrl: 'customer_component/address-book.html',
            controller: 'addressBookCtrl'
        }).when('/account/:id/add-address', {
            templateUrl: 'customer_component/add-address.html',
            controller: 'addAddressCtrl'
        }).when('/account/:id/address/:addressId/edit', {
            templateUrl: 'customer_component/edit-address.html',
            controller: 'editAddressCtrl'
        }).when('/account/:id/orders', {
            templateUrl: 'customer_component/orders.html',
            controller: 'orderCtrl'
        }).when('/account/:id/orders/:orderNumber', {
            templateUrl: 'customer_component/order-details.html',
            controller: 'orderDetailsCtrl'
        }).when('/account/:id/favourites', {
            templateUrl: 'customer_component/favourites.html',
            controller: 'favouriteCtrl'
        }).when('/account/:id/vouchers', {
            templateUrl: 'customer_component/vouchers.html',
            controller: 'voucherCtrl'
        });
    });