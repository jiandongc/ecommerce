var auth = angular.module('auth', ['ngCookies']);

auth.factory('authService', function($http, $cookies, $location, $rootScope, $q, customersFactory, cartSummaryFactory, environment){

	var authenticateUser = function (credentials){
		validateUser(credentials)
        .then(getCustomerByEmail)
        .then(syncCustomerAndCart)
        .then(redirectToAccountPage, loginFailed);
	};

    var validateUser = function(credentials){
        var data = 'grant_type=password&username='+credentials.email+'&password='+credentials.password;
        var configs = {headers: {'Content-Type' : 'application/x-www-form-urlencoded'}}
        $cookies.put('access_token', 'Basic '+ btoa('client:secret'));

        // return a derived promise 
        return $http.post(environment.authServerUrl + '/uaa/oauth/token', data, configs).then(function(response){
            $cookies.put('access_token', 'Bearer ' + response.data.access_token);
            return credentials;
        }, function(error){
            return $q.reject("authentication failed for user"); 
        });
    };

    var getCustomerByEmail = function(credentials){
        return customersFactory.get({email:credentials.email}).$promise.then(function(customer){
            $cookies.put('current_user', customer.name);
            return customer;
        }, function(error){
            return $q.reject("customer not found");
        });
    };

    var syncCustomerAndCart = function(customer) {
        if(typeof $cookies.get('cart_uid') !== "undefined") {
            var configs = {headers: {'Content-Type' : 'application/json'}};
            var cartUid = $cookies.get('cart_uid');
            return $http.put(environment.orderUrl + '/anoncarts/' + cartUid, customer.id, configs).then(function(response){
                return customer;
            }, function(error){
                return $q.reject("update shopping cart failed");
            })
        } else {
            return cartSummaryFactory.get({customerId:customer.id}).$promise.then(function(response){
                $cookies.put('cart_uid', response.cartUid);
                return customer;
            }, function(error){
                return customer;
            });
        }
    };

    var redirectToAccountPage = function(customer){
        $rootScope.loginError = false;
        $location.path("/account/" + customer.id);
    };

    var loginFailed = function(error){
        $rootScope.loginError = true;
    };

	return {
        authenticateUser : authenticateUser
    };
});

auth.factory('oauthTokenInterceptor', function($cookies, $location, $q){
	var service = this;

    service.request = function(config) {
    	config.headers.authorization = $cookies.get('access_token');
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



auth.config(['$httpProvider', function($httpProvider) {  
    $httpProvider.interceptors.push('oauthTokenInterceptor');
}]);
