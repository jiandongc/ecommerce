var home = angular.module('home', ['ngRoute', 'ngCookies']);

home.controller('homeCtrl', function($scope, $location, reviewFactory) {

  reviewFactory.getFeedback(undefined, undefined, 0, 1).then(function(response){
    $scope.feedbackSize = '(' + response.size + ')';
  });

  $scope.addFeedback = function(feedback){
    reviewFactory.addFeedback(feedback).then(function(response){
      $location.path("/feedback_received/" + response._id);
    });
  }

});

home.component('productpanel', {
  templateUrl: 'component/product-panel.html',
  controller: function($scope, $element, productFactory){
    $scope.tag = $element.attr("tag");

    productFactory.getProductsWithTag($scope.tag).then(function(response){
        $scope.products = response;
    });

    $scope.refresh = function(categoryCode){
      $scope.loading = 'opacity-05';
      if(categoryCode == undefined){
          productFactory.getProductsWithTag($scope.tag).then(function(response){
              $scope.loading = '';
              $scope.products = response;
          });
      } else {
          productFactory.getProductsWithTagInCategory($scope.tag, categoryCode).then(function(response){
              $scope.loading = '';
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
