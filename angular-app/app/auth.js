var auth = angular.module('auth', ['ngCookies']);

auth.factory('authService', function($localstorage, $location, $rootScope, $q, authFactory, customerFactory, shoppingCartFactory){

	var authenticateUser = function (credentials){
		validateUser(credentials)
        .then(getCustomerByEmail)
        .then(syncCustomerAndCart)
        .then(redirectToAccountPage, loginFailed);
	};

    var validateUser = function(credentials){
        return authFactory.validateUser(credentials).then(function(response){
            $localstorage.set('access_token', response.headers("Authentication"));
            return credentials;
        }, function(error){
            return $q.reject("authentication failed for user"); 
        });
    };

    var getCustomerByEmail = function(credentials){
        return customerFactory.getCustomerByEmail(credentials).then(function(customer){
            $localstorage.set('current_user', customer.name);
            $localstorage.set('customer_id', customer.id);
            return customer;
        }, function(error){
            return $q.reject("customer not found");
        });
    };

    var syncCustomerAndCart = function(customer) {
        if($localstorage.containsKey('cart_uid')) {
            var cartUid = $localstorage.get('cart_uid');
            return shoppingCartFactory.updateCustomerId(cartUid, customer.id).then(function(response){
                return customer;
            }, function(error){
                return customer;
            })
        } else {
            return shoppingCartFactory.getShoppingCartByCustomerId(customer.id).then(function(data){
                $localstorage.set('cart_uid', data.shoppingCart.cartUid);
                return customer;
            }, function(error){
                return customer;
            })
        }
    };

    var redirectToAccountPage = function(customer){
        $rootScope.loginError = false;
        $location.path("/account/" + customer.id);
    };

    var loginFailed = function(error){
        $rootScope.loginError = true;
        $rootScope.$broadcast('reset');
    };

    var assignGuestToken = function(){
        if(!$localstorage.containsKey('access_token')) {
            $localstorage.set('access_token', "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsInJvbGVzIjpbImd1ZXN0Il0sImV4cCI6MTg1MTI0ODI4MX0.UseykStWF5Qk6a_S65tLgSTDb2BAY1fDz0bEGeUr_EafioOE-I1cpGBwxt-gwLUYlYfo_bNr0PBuAIRVdHtYoQ");
        }
    }

	return {
        authenticateUser : authenticateUser,
        assignGuestToken : assignGuestToken
    };
});

auth.factory('authFactory', function($http, environment){
    var validateUser = function(credentials){
        var data = {
            username : credentials.email,
            password : credentials.password
        };

        var configs = {headers: {'Content-Type' : 'application/json'}};

        return $http.post(environment.authServerUrl + '/login', data, configs).then(function(response){
            return response;
        });
    };

    return {
        validateUser: validateUser
    }
});