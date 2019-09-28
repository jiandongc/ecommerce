var productDetail = angular.module('productDetail', ['ngRoute']);

productDetail.controller('productDetailCtrl', function($scope, $http, $routeParams, environment, $timeout, shoppingCartFactory, $localstorage, $rootScope) {
  $scope.addingItem = false;
  $scope.selectOptionAlert = false;
  $scope.loading = true;
	$http.get(environment.productUrl + '/products/' + $routeParams.code).then(function(response){
		$scope.product = response.data;
    $scope.loading = false;

    $http.get(environment.productUrl + '/categories/' + response.data.categoryCode).then(function(response){
      $scope.parentcategories = response.data.parents;
    });

    $scope.price = response.data.price;
    $scope.originalPrice = response.data.originalPrice;
    $scope.onSale = response.data.onSale;
    $scope.discountRate = response.data.discountRate;
    
    if($scope.product.variants.length == 1){
      var variant = $scope.product.variants[0];
      $scope.sku = variant.sku;
      $scope.description = variant.description;
      $scope.qty = variant.qty;
    } else {
      $scope.sku = undefined;
      $scope.description = undefined;
      $scope.qty = undefined;
    }

    $scope.selected = {};
    for(var attribute in $scope.product.attributes){
      $scope.selected[attribute] = '';
    }
	});

  $http.get(environment.productUrl + '/products/color/' + $routeParams.code).then(function(response){
    $scope.colorVariant = response.data;
  });

  $scope.select=function(attribute, value){
    $scope.selected[attribute] = value;
    var variant = $scope.find();

    if(variant){
      $scope.price = variant.price;
      $scope.originalPrice = variant.originalPrice;
      $scope.onSale = variant.isOnSale;
      $scope.discountRate = variant.discountRate;
      $scope.sku = variant.sku;
      $scope.description = variant.description;
      $scope.qty = variant.qty;
      $scope.selectOptionAlert = false;
    } else {
      $scope.price = $scope.product.price;
      $scope.originalPrice = $scope.product.originalPrice;
      $scope.onSale = $scope.product.onSale;
      $scope.discountRate = $scope.product.discountRate;
      $scope.sku = undefined;
      $scope.description = undefined;
      $scope.qty = undefined;
      $scope.selectOptionAlert = true;
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

      if(typeof $scope.sku === "undefined" || typeof $scope.price === "undefined"){
        $scope.selectOptionAlert = true;
        return;
      }

      var imageUrl = product.images[0];
      $scope.addingItem = true;
      if(!$localstorage.containsKey("cart_uid")){
          if($localstorage.containsKey("customer_id")){
              shoppingCartFactory.createShoppingCartForCustomer($localstorage.get("customer_id")).then(function(cartUid){
                  shoppingCartFactory.addItemToShoppingCart(product.name, product.code, $scope.price, imageUrl, $scope.sku, $scope.description, cartUid).then(function(data){
                      $localstorage.set('cart_uid', cartUid);
                      $rootScope.$broadcast('updateCartSummary', true);
                      $scope.addingItem = false;
                  });
              }); 
          } else {
              shoppingCartFactory.createShoppingCartForGuest().then(function(cartUid){
                  shoppingCartFactory.addItemToShoppingCart(product.name, product.code, $scope.price, imageUrl, $scope.sku, $scope.description, cartUid).then(function(data){
                      $localstorage.set('cart_uid', cartUid);
                      $rootScope.$broadcast('updateCartSummary', true);
                      $scope.addingItem = false;
                  });
              }); 
          }
      } else {
          var cartUid = $localstorage.get('cart_uid', undefined);
          shoppingCartFactory.addItemToShoppingCart(product.name, product.code, $scope.price, imageUrl, $scope.sku, $scope.description, cartUid).then(function(data){
              $rootScope.$broadcast('updateCartSummary', true);
              $scope.addingItem = false;
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