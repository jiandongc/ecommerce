var auth = angular.module('auth', ['ngCookies']);

auth.factory('authService', function($http, $cookies, $location, $rootScope, $q, customersFactory){

	var authenticateUser = function (credentials){
		validateUser(credentials)
        .then(getCustomerByEmail)
        .then(updateCartAndDirectToAccountPage, showLogInErrorMessage);
	};

    var validateUser = function(credentials){
        var data = 'grant_type=password&username='+credentials.email+'&password='+credentials.password;
        var configs = {headers: {'Content-Type' : 'application/x-www-form-urlencoded'}}
        $cookies.access_token = 'Basic '+ btoa('client:secret');

        // return a derived promise 
        return $http.post('http://localhost:9999/uaa/oauth/token', data, configs).then(function(response){
            $cookies.access_token = 'Bearer ' + response.data.access_token;
            return credentials;
        }, function(error){
            return $q.reject("authentication failed for user"); 
        });
    };

    var getCustomerByEmail = function(credentials){
        return customersFactory.get({email:credentials.email}).$promise.then(function(customer){
            $cookies.currentUser = customer.name;
            return customer;
        }, function(error){
            return $q.reject("customer not found");
        });
    };

    var updateCartAndDirectToAccountPage = function(customer){
        $rootScope.loginError = false;
        if(typeof $cookies.cart_uid !== "undefined"){
            var configs = {headers: {'Content-Type' : 'application/json'}};
            $http.put('http://localhost:8082/anoncarts/'+$cookies.cart_uid, customer.id, configs);
        }
        
        $location.path("/account/" + customer.id);
    };

    var showLogInErrorMessage = function(error){
        $rootScope.loginError = true;
    };

	return {
        authenticateUser : authenticateUser
    };
});

auth.factory('oauthTokenInterceptor', function($cookies, $location, $q){
	var service = this;

    service.request = function(config) {
    	config.headers.authorization = $cookies.access_token;
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
