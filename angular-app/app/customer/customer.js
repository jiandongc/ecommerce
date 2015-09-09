var customer = angular.module('customer', ['ngRoute','ngResource','ngCookies']);

customer.controller('loginCtrl', function($scope, customersFactory, authService, $rootScope) {
    
    $rootScope.loginError = false;

	$scope.registerCustomer = function(){
        var newCustomer = {
            name : $scope.customer.name,
            email : $scope.customer.email,
            password : $scope.customer.password
        };
        customersFactory.save(newCustomer, function(data){
            $scope.login(data);
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
  function($routeProvider) {

    $routeProvider.
      when('/login', {
        templateUrl: 'customer/login.html',
        controller: 'loginCtrl'
      }).when('/account/:id', {
        templateUrl: 'customer/account.html',
        controller: 'accountCtrl'
      });

});



