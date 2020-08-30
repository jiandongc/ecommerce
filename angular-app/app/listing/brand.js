var brand = angular.module('brand', ['ngRoute']);

brand.controller('brandCtl', function($scope, $routeParams, $window, brandFactory, productFactory) {
	brandFactory.getBrandWithCode($routeParams.code).then(function(response){
		$scope.brand = response;
		$window.document.title = $scope.brand.name + ' | Noodle Monster';
		$scope.brand.imageUrl = $scope.brand.imageUrl ? $scope.brand.imageUrl : '/images/brand/notfound.png';
	});

	$scope.loading = true;
	$scope.code = $routeParams.code;
	$scope.sort = {display: "Our favourites", code: undefined};

	productFactory.getProductsWithBrand($scope.code).then(function(response){
		$scope.products = response;
		$scope.size = response.length;
		$scope.loading = false;
	});

	$scope.sortby = function(display, sortcode){
		$scope.loading = true;
    	$scope.sort = {display: display, code: sortcode};
    	productFactory.getProductsWithBrandInOrder($scope.code, sortcode).then(function(response){
			$scope.products = response;
			$scope.size = response.length;
			$scope.loading = false;
		});
  	}
});

brand.controller('brandListCtl', function($scope, $window, brandFactory) {
    $window.document.title = 'Brands | Noodle Monster';
	$scope.loading = true;
	brandFactory.getAllBrands().then(function(response){
		$scope.brands = response;
		for (var i in $scope.brands) {
      		$scope.brands[i].imageUrl = $scope.brands[i].imageUrl ? $scope.brands[i].imageUrl : '/images/brand/notfound.png';
    	}
    	$scope.loading = false;
	});
});

brand.factory('brandFactory', function($http, environment){
  var getBrandWithCode = function(brandCode){
    return $http.get(environment.productUrl + '/brands/' + brandCode).then(function(response){
      return response.data;
    });
  }

  var getAllBrands = function(){
    return $http.get(environment.productUrl + '/brands').then(function(response){
      return response.data;
    });  	
  }

  var getAllBrandsInRandomOrder = function(){
    return $http.get(environment.productUrl + '/brands?sort=random').then(function(response){
      return response.data;
    });
  }

  return {
    getBrandWithCode: getBrandWithCode,
    getAllBrands: getAllBrands,
    getAllBrandsInRandomOrder: getAllBrandsInRandomOrder
  }
});

brand.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/brands/:code', {
        templateUrl: 'listing/brand.html',
        controller: 'brandCtl'
      }).when('/brands', {
        templateUrl: 'listing/brand-list.html',
        controller: 'brandListCtl'
      });
  }]);
