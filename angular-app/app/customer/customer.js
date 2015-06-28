var customer = angular.module('customer', ['ngRoute','ngResource','ngCookies']);

customer.controller('loginCtrl', function($scope, $location, customersFactory, $http, $cookies, $timeout, authService) {

	$scope.registerCustomer = function(){
		authService.anonymousAccess(function(){
            var newCustomer = {
                name : $scope.customer.name,
                email : $scope.customer.email,
                password : $scope.customer.password
            };
            customersFactory.save(newCustomer, function(data){
                $scope.login(data);
            });
        });
	}

	$scope.login = function(credentials){
        authService.authenticateUser(credentials);
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



