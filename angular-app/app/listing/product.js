var productDetail = angular.module('productDetail', ['ngRoute']);

productDetail.controller('productDetailCtrl', function($scope, $http, $routeParams, environment, $timeout) {
	$http.get(environment.productUrl + '/products/' + $routeParams.code).success(function(response){
		$scope.product = response;
    
    $http.get(environment.productUrl + '/categories/' + response.categoryCode).success(function(response){
      $scope.parentcategories = response.parents;
    });

    $scope.price = response.price;
    if($scope.product.variants.length == 1){
      var variant = $scope.product.variants[0];
      if(variant.qty <= 10){
        $scope.lowqty = variant.qty;
      }
    }

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
      $scope.lowqty = variant.qty;
    } else {
      $scope.lowqty = undefined;  
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
  };

  $scope.$on('initSlider', function() {
      $('.flexslider').flexslider({
            animation: 'slide', 
            controlNav: 'thumbnails', 
            slideshow: false
          });
  });
});

productDetail.directive('onFinishInitSlider', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
          if (scope.$last === true) {
            $timeout(function () {
              $('.flexslider').flexslider({
                animation: 'slide', 
                controlNav: 'thumbnails', 
                slideshow: false
              });
            });
          }
        }
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