var category = angular.module('category', ['ngRoute']);

category.controller('categoryCtrl', function($scope, $http, $routeParams, cartService, environment) {
  $scope.processing = {};
  $scope.selectedBrands = {};
  $scope.property = 'id';
  $scope.reverse = true;

	$http.get(environment.productUrl + '/products/?cc=' + $routeParams.code).success(function(response){
		$scope.products = response;
    angular.forEach($scope.products,function(value,index){
      value.quantity = 1;
    });
	});

  $http.get(environment.productUrl + '/categories/' + $routeParams.code).success(function(response){
    $scope.code = response.code;
    $scope.name = response.name;
    $scope.productTotal = response.productTotal;
    $scope.parentcategories = response.parents;
    $scope.subcategories = response.children;
  });

  $scope.brandfilter=function(item){
    return $scope.selectedBrands[item.brand] || $scope.noFilter($scope.selectedBrands);
  };

  $scope.noFilter=function (filterObj){
    return Object.keys(filterObj).every(function(key){
     return !filterObj[key]; 
    });
  };

  $scope.sortBy = function(property){
    $scope.reverse = ($scope.property === property) ? !$scope.reverse : true;
    $scope.property = property;
  };

  $scope.addItemToCart = function(product){
    cartService.addItem(product, function(){
      $scope.processing[product.code]=false;
    });
  };
});

category.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/category/:code', {
        templateUrl: 'listing/category.html',
        controller: 'categoryCtrl'
      });
}]);
