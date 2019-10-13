var productTag = angular.module('productTag', ['ngRoute']);

productTag.controller('tagsCtrl', function($scope, $routeParams, productTagFactory) {
	$scope.loading = true;
	$scope.tag = $routeParams.tag;
	$scope.code = $routeParams.code;
	$scope.sort = {display: "Our favourites", code: undefined};

	productTagFactory.getProducts($scope.code).then(function(response){
		$scope.products = response;
		$scope.loading = false;
	});

	$scope.sortby = function(display, sortcode){
		$scope.loading = true;
    	$scope.sort = {display: display, code: sortcode};
    	productTagFactory.getProductsWithSort($scope.code, sortcode).then(function(response){
			$scope.products = response;
			$scope.loading = false;
		});
  	}

});

productTag.factory('productTagFactory', function($http, environment){
  var getProducts = function(tag){
    return $http.get(environment.productUrl + '/products?tg=' + tag).then(function(response){
      return response.data;
    });
  }

  var getProductsWithSort = function(tag, sort){
    return $http.get(environment.productUrl + '/products?tg=' + tag + '&sort=' + sort).then(function(response){
      return response.data;
    });
  }

  return {
    getProducts: getProducts,
    getProductsWithSort: getProductsWithSort
  }
});

productTag.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/tags/:code/:tag', {
        templateUrl: 'listing/tags.html',
        controller: 'tagsCtrl'
      });
  }]);
