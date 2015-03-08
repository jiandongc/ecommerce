var customer = angular.module('customer', ['ngRoute','ngResource']);

customer.controller('customerCtrl', function($scope, customersFactory) {
	$scope.customer = {};
	$scope.registerCustomer = function(isValid){
		if (isValid) {
			var newCustomer = {
					name : $scope.customer.name,
					email : $scope.customer.email,
					password : $scope.customer.password
			};
			customersFactory.save(newCustomer);
		}
	}
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
 
                //get the value of the first password
                var e1 = scope.$eval(attrs.ngModel);
 
                //get the value of the other password 
                var e2 = scope.$eval(attrs.passwordMatch);
                return e1 == e2;
            };
            scope.$watch(checker, function (n) {
 
                //set the form control to valid if both
                //passwords are the same, else invalid
                control.$setValidity("unique", n);
            });
        }
    };
}]);

customer.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/login', {
        templateUrl: 'customer/login.html',
        controller: 'customerCtrl'
      });
}]);


