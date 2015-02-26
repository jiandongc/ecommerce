var app = angular.module('myApp', [
	'ngRoute',
	'allProduct',
	'productDetail',
	'customer'
]);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      otherwise({
        redirectTo: '/products'
      });
  }]);
