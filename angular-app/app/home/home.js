var home = angular.module('home', ['ngRoute', 'ngCookies']);

home.controller('homeCtrl', function($scope) {

});

home.component('productpanel', {
  templateUrl: 'component/product-panel.html',
  controller: function($scope, $element, productFactory){
    $scope.tag = $element.attr("tag");

    productFactory.getProductsWithTag($scope.tag).then(function(response){
        $scope.products = response;
    });

    $scope.refresh = function(categoryCode){
      if(categoryCode == undefined){
          productFactory.getProductsWithTag($scope.tag).then(function(response){
              $scope.products = response;
          });
      } else {
          productFactory.getProductsWithTagInCategory($scope.tag, categoryCode).then(function(response){
              $scope.products = response;
          });
      }
    }
  },
  bindings: {title: '@', titlecolor: '@'}
});

home.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/home', {
        templateUrl: 'home/home.html',
        controller: 'homeCtrl'
      });
}]);
