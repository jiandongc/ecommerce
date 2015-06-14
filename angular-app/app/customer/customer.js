var customer = angular.module('customer', ['ngRoute','ngResource','ngCookies']);

customer.controller('loginCtrl', function($scope, $location, customersFactory, $http, $cookies, $timeout) {

	$scope.registerCustomer = function(isValid){
		if (isValid) {
			var data = 'grant_type=client_credentials';
			var configs = {headers: {'Content-Type' : 'application/x-www-form-urlencoded'}}
			$cookies.access_token = 'Basic '+ btoa('newClient:newSecret');

	    var success = function (data) {
				$cookies.access_token = 'Bearer ' + data.access_token;
				var newCustomer = {
					name : $scope.customer.name,
					email : $scope.customer.email,
					password : $scope.customer.password
				};
				customersFactory.save(newCustomer, function(data){
					$scope.login(data);
				});
	    };

			$http.post('http://localhost:9999/uaa/oauth/token', data, configs).success(success);
		}
	}

	$scope.login = function(credentials){
		var data = 'grant_type=password&username='+credentials.email+'&password='+credentials.password;
		var configs = {headers: {'Content-Type' : 'application/x-www-form-urlencoded'}}
		$cookies.access_token = 'Basic '+ btoa('client:secret');

    var success = function (data) {
			$cookies.access_token = 'Bearer ' + data.access_token;
			customersFactory.get({email:credentials.email}, function(response){
        $cookies.currentUser = response.name;
        $location.path("/account/" + response.id);				
			});
    };

		$http.post('http://localhost:9999/uaa/oauth/token', data, configs).success(success);
	}

});

customer.controller('accountCtrl', function($scope, $routeParams, customersFactory){
	customersFactory.get({id: $routeParams.id}, function(response){
		$scope.customer = response;
	});
});

customer.factory('customersFactory', function($resource){
	return $resource('http://localhost:8081/customers/:id');
});

customer.service('oauthTokenInterceptor', function ($cookies) {
	var service = this;
	service.request = function(config) { 
		config.headers.authorization = $cookies.access_token;
    return config;
  };
});

customer.directive('passwordMatch', [function () {
    return {
        restrict: 'A',
        scope:true,
        require: 'ngModel',
        link: function (scope, elem , attrs,control) {
            var checker = function () {
                var e1 = scope.$eval(attrs.ngModel); 
                var e2 = scope.$eval(attrs.passwordMatch);
                return e1 == e2;
            };
            scope.$watch(checker, function (n) {
                 control.$setValidity("unique", n);
            });
        }
    };
}]);

customer.config(
  function($routeProvider, $httpProvider) {

    $routeProvider.
      when('/login', {
        templateUrl: 'customer/login.html',
        controller: 'loginCtrl'
      }).when('/account/:id', {
        templateUrl: 'customer/account.html',
        controller: 'accountCtrl'
      });

		$httpProvider.interceptors.push('oauthTokenInterceptor');
});



