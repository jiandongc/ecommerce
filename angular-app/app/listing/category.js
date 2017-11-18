var category = angular.module('category', ['ngRoute', 'checklist-model']);

category.controller('categoryCtrl', function($scope, $routeParams, categoryService, cartService) {
  $scope.processing = {};
  $scope.selectedBrands = {};
  $scope.filters = {};
  $scope.property = 'id';
  $scope.reverse = true;

  categoryService.findCategory($routeParams.code).success(function(response){
    $scope.code = response.code;
    $scope.name = response.name;
    $scope.productTotal = response.productTotal;
    $scope.parentcategories = response.parents;
    $scope.subcategories = response.children;
  });

  $scope.findProducts = function(categoryCode, filters) {
    categoryService.findProducts(categoryCode, filters).success(function(response){
      $scope.filters = {};
      $scope.products = response.products;
      $scope.facets = response.facets;
      for(var i=0; i<response.facets.length; i++){
        var facet = response.facets[i];
        if(!facet.hasSelectedValue) {continue;}
        var selected = [];
        for(var j=0; j<facet.facetValues.length; j++){
          var facetValue = facet.facetValues[j];
          if(!facetValue.selected) {continue;}
          selected.push(facetValue.name);
        }
        if(selected.length > 0){
          $scope.filters[facet.name] = selected;
        }
      }
    });
  }

  $scope.findProducts($routeParams.code, $scope.filters);

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

  $scope.applyFilters = function(){
    for(var key in $scope.filters){
      if($scope.filters[key].length == 0){
        delete $scope.filters[key];
      }
    }
    $scope.findProducts($scope.code, $scope.filters);
  };

  $scope.style = function(value){
    if(value%3==0) {
        return "btn-success";
     } else if(value%3==1) {
        return "btn-warning";
     } else {
        return "btn-danger";
     }
  };


});

category.service('categoryService', function($http, environment){
  this.findProducts = function(categoryCode, filters) {
    return $http.get(environment.productUrl + '/products/search/categories/' + categoryCode, {params: {filter: filters}});
  };

  this.findCategory = function(categoryCode){
    return $http.get(environment.productUrl + '/categories/' + categoryCode);  
  }
});

category.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/category/:code', {
        templateUrl: 'listing/category.html',
        controller: 'categoryCtrl'
      });
}]);
