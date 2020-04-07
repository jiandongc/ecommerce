var auth = angular.module('auth', ['ngCookies']);

auth.factory('authService', function($localstorage, $location, $rootScope, $q, authFactory, customerFactory, shoppingCartFactory) {

    var authenticateUser = function(credentials) {
        validateUser(credentials)
            .then(getCustomerByEmail)
            .then(syncCustomerAndCart)
            .then(redirectToAccountPage, loginFailed);
    };

    var validateUser = function(credentials) {
        return authFactory.validateUser(credentials).then(function(response) {
            $localstorage.set('access_token', response.headers("Authentication"));
            return credentials;
        }, function(error) {
            return $q.reject("authentication failed for user");
        });
    };

    var getCustomerByEmail = function(credentials) {
        return customerFactory.getCustomerByEmail(credentials).then(function(customer) {
            $localstorage.set('current_user', customer.name);
            $localstorage.set('customer_id', customer.id);
            return customer;
        }, function(error) {
            return $q.reject("customer not found");
        });
    };

    var syncCustomerAndCart = function(customer) {
        if ($localstorage.containsKey('cart_uid')) {
            var cartUid = $localstorage.get('cart_uid');
            return shoppingCartFactory.addCustomerInfo(cartUid, customer).then(function(response) {
                return customer;
            }, function(error) {
                return customer;
            })
        } else {
            return shoppingCartFactory.getShoppingCartByCustomerId(customer.id).then(function(data) {
                $localstorage.set('cart_uid', data.cartUid);
                return customer;
            }, function(error) {
                return customer;
            })
        }
    };

    var redirectToAccountPage = function(customer) {
        $rootScope.loginError = false;
        $rootScope.logining = false;
        $location.path("/account/" + customer.id);
    };

    var loginFailed = function(error) {
        $rootScope.loginError = true;
        $rootScope.logining = false;
        $rootScope.$broadcast('resetUserInfo');
    };

    return {
        authenticateUser: authenticateUser
    };
});

auth.factory('authFactory', function($injector, environment) {
    var validateUser = function(credentials) {
        var data = {
            username: credentials.email,
            password: credentials.password
        };

        var configs = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        return $injector.get('$http').post(environment.authServerUrl + '/login', data, configs).then(function(response) {
            return response;
        });
    };

    var downloadGuestToken = function() {
        return $injector.get('$http').get(environment.authServerUrl + '/guesttoken').then(function(response) {
            return response;
        });
    };

    return {
        validateUser: validateUser,
        downloadGuestToken: downloadGuestToken
    };
});