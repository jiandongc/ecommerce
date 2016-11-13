var category = angular.module('category', ['ngRoute']);

category.controller('categoryCtrl', function($scope, $http, $routeParams, environment) {
	$http.get(environment.productUrl + '/categories/' + $routeParams.id).success(function(response){
    $scope.categoryname = response.categoryName;
		$scope.products = response.products;
    $scope.subcategories = response.subCategories;
    $scope.brands = response.brands;
    $scope.productcount = response.productCount;
    $scope.parentcategories = response.parentCategories;

	});
});

category.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/category/:id', {
        templateUrl: 'listing/category.html',
        controller: 'categoryCtrl'
      });
  }]);
