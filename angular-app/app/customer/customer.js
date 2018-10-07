var customer = angular.module('customer', ['ngRoute','ngResource']);

customer.controller('loginCtrl', function($scope, authService, $rootScope) {
    
    authService.assignGuestToken();
    $rootScope.loginError = false;

	$scope.login = function(credentials){
        authService.authenticateUser(credentials);
	};
});

customer.controller('registerCtrl', function($scope, authService, customerFactory) {
    
    authService.assignGuestToken();
    $scope.error = false;
    $scope.errorMsg = "";

    $scope.registerCustomer = function(){
        var newCustomer = {
            name : $scope.customer.name,
            email : $scope.customer.email,
            password : $scope.customer.password
        };

        customerFactory.save(newCustomer).then(function(data){
            $scope.error = false;
            var credentials = {
                email : data.email,
                password : newCustomer.password
            };
            authService.authenticateUser(credentials);
        }, function(error){
            $scope.error = true;
            if(error.status === 409){
                $scope.errorMsg = "This the email address is already used.";
            } else {
                $scope.errorMsg = "Technical error, please contact site admin: jiandong.c@gmail.com";
            }
        });
    };
});

customer.controller('accountCtrl', function($scope, $routeParams, customerFactory){
    customerFactory.getCustomerById($routeParams.id).then(function(response){
        $scope.customer = response;
    });
});

customer.factory('customerFactory', function($http, environment){

    var getCustomerByEmail = function(credentials){
        return $http.get(environment.customerUrl + '/customers?email=' + credentials.email).then(function(response){
            return response.data;
        });
    };

    var getCustomerById = function(id){
        return $http.get(environment.customerUrl + '/customers/' + id).then(function(response){
            return response.data;
        });
    };

    var save = function(customer){
        var configs = {headers: {'Content-Type' : 'application/json'}};
        return $http.post(environment.customerUrl + '/customers', customer, configs).then(function(response){
            return response.data;
        });
    }

    return {
        getCustomerByEmail: getCustomerByEmail,
        getCustomerById: getCustomerById,
        save: save
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
  function($routeProvider) {

    $routeProvider.
      when('/login', {
        templateUrl: 'customer/login.html',
        controller: 'loginCtrl'
      }).when('/register', {
        templateUrl: 'customer/register.html',
        controller: 'registerCtrl'
      }).when('/account/:id', {
        templateUrl: 'customer/account.html',
        controller: 'accountCtrl'
      });
});



