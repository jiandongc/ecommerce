var auth = angular.module('auth', ['ngCookies']);

auth.factory('authService', function($http, $cookies, $location, customersFactory){

	var authService = {};

	authService.authorize = function (callback) {
		if ($cookies.current_user === undefined && $cookies.access_token === undefined) {
			authService.anonymousAccess(callback);
		} else {
			callback && callback();
		}
	}


	authService.anonymousAccess = function (callback){
		delete $cookies['currentUser'];
		delete $cookies['access_token'];
		var data = 'grant_type=client_credentials';
		var configs = {headers: {'Content-Type' : 'application/x-www-form-urlencoded'}}
		$cookies.access_token = 'Basic '+ btoa('newClient:newSecret');
		$http.post('http://localhost:9999/uaa/oauth/token', data, configs).success(function(data){
			$cookies.access_token = 'Bearer ' + data.access_token;
			callback && callback();
		});
	}

	authService.authenticateUser = function (credentials){
		var data = 'grant_type=password&username='+credentials.email+'&password='+credentials.password;
		var configs = {headers: {'Content-Type' : 'application/x-www-form-urlencoded'}}
		$cookies.access_token = 'Basic '+ btoa('client:secret');
		$http.post('http://localhost:9999/uaa/oauth/token', data, configs).success(function(data){
			$cookies.access_token = 'Bearer ' + data.access_token;
			customersFactory.get({email:credentials.email}, function(response){
           		$cookies.currentUser = response.name;
        		$location.path("/account/" + response.id);
        	});
		});
	}

	return authService;
});
