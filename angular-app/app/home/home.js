var home = angular.module('home', ['ngRoute', 'ngCookies']);

home.service('productService', function($http, environment){
   this.getAllProducts = function() {
     return $http.get(environment.productUrl + '/products');
   };
});

home.controller('homeCtrl', function($scope, $http, environment, productService, authService) {
  $scope.processing = {};
  authService.assignGuestToken();

	$http.get(environment.productUrl + '/products/?cc=msg').then(function(response) {
		$scope.c1 = response.data;
		angular.forEach($scope.c1, function(value,index){
			value.quantity = 1;
    });
	});

  $http.get(environment.productUrl + '/products/?cc=mzg').then(function(response) {
    $scope.c2 = response.data;
    angular.forEach($scope.c2, function(value,index){
      value.quantity = 1;
    });
  });
});

home.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/home', {
        templateUrl: 'home/home.html',
        controller: 'homeCtrl'
      });
}]);
