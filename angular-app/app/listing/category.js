var category = angular.module('category', ['ngRoute', 'checklist-model']);

category.controller('categoryCtrl', function($scope, $routeParams, categoryService, authService) {
  $scope.processing = {};
  $scope.selectedBrands = {};
  $scope.filters = {};
  $scope.property = 'id';
  $scope.reverse = true;
  $scope.sort = {display: "Our favourites", code: undefined};

  authService.assignGuestToken();
  
  categoryService.findCategory($routeParams.code).then(function(response){
    var category = response.data;
    $scope.code = category.code;
    $scope.name = category.name;
    $scope.productTotal = category.productTotal;
    $scope.parentcategories = category.parents;
    $scope.subcategories = category.children;
  });

  $scope.findProducts = function(categoryCode, filters, sort) {
    categoryService.findProducts(categoryCode, filters, sort).then(function(response){
      var category = response.data;
      $scope.filters = {};
      $scope.products = category.products;
      $scope.facets = category.facets;
      for(var i=0; i<category.facets.length; i++){
        var facet = category.facets[i];
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

  $scope.findProducts($routeParams.code, $scope.filters, $scope.sort);

  $scope.applyFilters = function(){
    for(var key in $scope.filters){
      if($scope.filters[key].length == 0){
        delete $scope.filters[key];
      }
    }
    $scope.findProducts($scope.code, $scope.filters, $scope.sort);
  }

  $scope.style = function(value){
    if(value%3==0) {
        return "btn-success";
     } else if(value%3==1) {
        return "btn-warning";
     } else {
        return "btn-danger";
     }
  }

  $scope.sortby = function(display, code){
    $scope.sort = {display: display, code: code};
    $scope.findProducts($scope.code, $scope.filters, $scope.sort);
  }

});

category.service('categoryService', function($http, environment){
  this.findProducts = function(categoryCode, filters, sort) {
    var params = {};
    
    if(typeof filters !== "undefined" && Object.keys(filters).length !== 0){
      params['filter'] = filters;
    }

    if(typeof sort !== "undefined" && typeof sort.code !== "undefined"){
      params['sort'] = sort.code;
    }

    return $http.get(environment.productUrl + '/products/search/categories/' + categoryCode, {params: params});
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
