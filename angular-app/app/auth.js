var auth = angular.module('auth', ['ngCookies']);

auth.factory('authService', function($http, $cookies, $location, $rootScope, customersFactory){

	var authService = {};

	authService.authenticateUser = function (credentials){
		var data = 'grant_type=password&username='+credentials.email+'&password='+credentials.password;
		var configs = {headers: {'Content-Type' : 'application/x-www-form-urlencoded'}}
		$cookies.access_token = 'Basic '+ btoa('client:secret');
		$http.post('http://localhost:9999/uaa/oauth/token', data, configs).success(function(data){
			$rootScope.loginError = false;
			$cookies.access_token = 'Bearer ' + data.access_token;
			customersFactory.get({email:credentials.email}, function(response){
           		$cookies.currentUser = response.name;
        		$location.path("/account/" + response.id);
        	});
		}).error(function(error){$rootScope.loginError = true;});
	}

	return authService;
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
