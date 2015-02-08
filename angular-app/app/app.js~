var app = angular.module('myApp', [
	'ngRoute',
	'allProduct',
	'productDetail'
]);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      otherwise({
        redirectTo: '/products'
      });
  }]);
