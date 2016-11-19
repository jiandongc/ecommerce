var category = angular.module('category', ['ngRoute']);

category.controller('categoryCtrl', function($scope, $http, $routeParams, environment) {
	$http.get(environment.productUrl + '/categories/' + $routeParams.id).success(function(response){
    $scope.categoryname = response.categoryName;
		$scope.products = response.products;
    $scope.subcategories = response.subCategories;
    $scope.brands = response.brands;
    $scope.productcount = response.productCount;
    $scope.parentcategories = response.parentCategories;
    $scope.selectedBrands = {};
    $scope.property = 'id';
    $scope.reverse = true;
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

});

category.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/category/:id', {
        templateUrl: 'listing/category.html',
        controller: 'categoryCtrl'
      });
}]);
