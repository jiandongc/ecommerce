var productDetail = angular.module('productDetail', ['ngRoute']);

productDetail.controller('productDetailCtrl', function($scope, $http, $routeParams, environment, $timeout, authService, shoppingCartFactory, $localstorage, $rootScope) {

  authService.assignGuestToken();
  
	$http.get(environment.productUrl + '/products/' + $routeParams.code).then(function(response){
		$scope.product = response.data;

    $http.get(environment.productUrl + '/products/color/' + $routeParams.code).then(function(response){
      $scope.colorVariant = response.data;
    });
    
    $http.get(environment.productUrl + '/categories/' + response.categoryCode).then(function(response){
      $scope.parentcategories = response.data.parents;
    });

    $scope.price = response.data.price;
    if($scope.product.variants.length == 1){
      var variant = $scope.product.variants[0];
      $scope.sku = variant.sku;
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
      $scope.sku = variant.sku;
    } else {
      $scope.price = $scope.product.price;
      $scope.sku = undefined;
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

  $scope.addItemToCart = function(product){
      var imageUrl = product.images.Main[0];
      if(!$localstorage.containsKey("cart_uid")){
          if($localstorage.containsKey("customer_id")){
              shoppingCartFactory.createShoppingCartForCustomer($localstorage.get("customer_id")).then(function(cartUid){
                  shoppingCartFactory.addItemToShoppingCart(product.name, $scope.price, imageUrl, $scope.sku, cartUid).then(function(data){
                      $localstorage.set('cart_uid', cartUid);
                      $rootScope.$broadcast('updateCartSummary', true);
                  });
              }); 
          } else {
              shoppingCartFactory.createShoppingCartForGuest().then(function(cartUid){
                  shoppingCartFactory.addItemToShoppingCart(product.name, $scope.price, imageUrl, $scope.sku, cartUid).then(function(data){
                      $localstorage.set('cart_uid', cartUid);
                      $rootScope.$broadcast('updateCartSummary', true);
                  });
              }); 
          }
      } else {
          var cartUid = $localstorage.get('cart_uid', undefined);
          shoppingCartFactory.addItemToShoppingCart(product.name, $scope.price, imageUrl, $scope.sku, cartUid).then(function(data){
              $rootScope.$broadcast('updateCartSummary', true);
          });  
      }

  };
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

productDetail.directive('dynamic', function ($compile) {
  return {
    restrict: 'A',
    replace: true,
    link: function (scope, ele, attrs) {
      scope.$watch(attrs.dynamic, function(html) {
        ele.html(html);
        $compile(ele.contents())(scope);
      });
    }
  };
});

productDetail.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/products/:code', {
        templateUrl: 'listing/product.html',
        controller: 'productDetailCtrl'
      });
  }]);