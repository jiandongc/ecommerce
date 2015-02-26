var customer = angular.module('customer', ['ngRoute']);

customer.controller('customerCtrl', function() {});

customer.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/login', {
        templateUrl: 'customer/login.html',
        controller: 'customerCtrl'
      });
}]);


