var productDetail = angular.module('productDetail', ['ngRoute']);

productDetail.controller('productDetailCtrl', function($scope, $http, $routeParams, environment) {
	$http.get(environment.productUrl + '/products/' + $routeParams.id).success(function(response){
		$scope.product = response;
	});
});

productDetail.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/products/:id', {
        templateUrl: 'listing/product.html',
        controller: 'productDetailCtrl'
      });
  }]);
