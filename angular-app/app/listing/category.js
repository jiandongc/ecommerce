var category = angular.module('category', ['ngRoute']);

category.controller('categoryCtrl', function($scope, $http, $routeParams, cartService, environment) {
	$http.get(environment.productUrl + '/products/?cc=' + $routeParams.code).success(function(response){
    $scope.categoryname = response.categoryName;
		$scope.products = response;
    $scope.subcategories = response.subCategories;
    $scope.brands = response.brands;
    $scope.productcount = response.productCount;
    $scope.parentcategories = response.parentCategories;
    $scope.selectedBrands = {};
    $scope.property = 'id';
    $scope.reverse = true;
    $scope.processing = {};

    angular.forEach($scope.products,function(value,index){
      value.quantity = 1;
    });
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
      $scope.processing[product.id]=false;
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
