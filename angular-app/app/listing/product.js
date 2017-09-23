var productDetail = angular.module('productDetail', ['ngRoute']);

productDetail.controller('productDetailCtrl', function($scope, $http, $routeParams, environment) {
	$http.get(environment.productUrl + '/products/' + $routeParams.code).success(function(response){
		$scope.product = response;
    $scope.price = $scope.product.price;
    $http.get(environment.productUrl + '/categories/' + response.categoryCode).success(function(response){
      $scope.parentcategories = response.parents;
    });

    $scope.selected = {};
    for(var attribute in $scope.product.attributes){
      $scope.selected[attribute] = '';
    }
	});

  $scope.select=function(attribute, value){
    $scope.selected[attribute] = value;
    var variant = $scope.find();

    if(variant){
      $scope.price = variant.price;
    } else {
      $scope.price = $scope.product.price;
    }

    if(variant && variant.qty <= 10){
      $scope.qty = variant.qty;
    } else {
      $scope.qty = undefined;  
    }
  };

  $scope.find=function(){
    outter:
    for(var i in $scope.product.variants){
      var variant = $scope.product.variants[i];
      for(var attribute in $scope.selected){
        if(variant[attribute] != $scope.selected[attribute]){
          continue outter;
        }
      }
      return variant;
    }
    return null;
  }
});



productDetail.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/products/:code', {
        templateUrl: 'listing/product.html',
        controller: 'productDetailCtrl'
      });
  }]);
