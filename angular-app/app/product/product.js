var productDetail = angular.module('productDetail', ['ngRoute']);

productDetail.controller('productDetailCtrl', function($scope, $http, $routeParams) {
	$http.get('http://localhost:8080/products/' + $routeParams.id).success(function(response){
		$scope.product = response;
	});
});

productDetail.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/products/:id', {
        templateUrl: 'product/product.html',
        controller: 'productDetailCtrl'
      });
  }]);
