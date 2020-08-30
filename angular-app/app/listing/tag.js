var productTag = angular.module('productTag', ['ngRoute']);

productTag.controller('tagCtrl', function($scope, $routeParams, $window, productFactory) {
	$scope.loading = true;
	$scope.tag = $routeParams.tag;
	$window.document.title = $scope.tag + ' | Noodle Monster';
	$scope.sort = {display: "Our favourites", code: undefined};
	$scope.category = {display: 'All', code: undefined};

	productFactory.getProductsWithTagInOrder($scope.tag, 'random').then(function(response){
		$scope.products = response;
		$scope.size = response.length;
		$scope.loading = false;
	});

	$scope.sortby = function(display, sortcode){
		$scope.refreshing = 'opacity-05';
    	$scope.sort = {display: display, code: sortcode};
    	if($scope.category.code == undefined){
            productFactory.getProductsWithTagInOrder($scope.tag, sortcode).then(function(response){
                $scope.products = response;
                $scope.size = response.length;
                $scope.refreshing = '';
            });
    	} else {
            productFactory.getProductsWithTagInCategoryInOrder($scope.tag, $scope.category.code, sortcode).then(function(response){
                $scope.products = response;
                $scope.size = response.length;
                $scope.refreshing = '';
            });
    	}
  	}

    $scope.refresh = function(category, categoryCode){
    	$scope.refreshing = 'opacity-05';
      	if(categoryCode == undefined){
      	    $scope.category = {display: 'All', code: undefined};
      	    if($scope.sort.code == undefined){
                productFactory.getProductsWithTag($scope.tag).then(function(response){
                    $scope.products = response;
                    $scope.size = response.length;
                    $scope.refreshing = '';
                });
      	    } else {
                productFactory.getProductsWithTagInOrder($scope.tag, $scope.sort.code).then(function(response){
                    $scope.products = response;
                    $scope.size = response.length;
                    $scope.refreshing = '';
                });
      	    }

      	} else {
      	    $scope.category = {display: category, code: categoryCode};
      	    if($scope.sort.code == undefined){
                productFactory.getProductsWithTagInCategory($scope.tag, categoryCode).then(function(response){
                    $scope.products = response;
                    $scope.size = response.length;
                    $scope.refreshing = '';
                });
      	    } else {
                productFactory.getProductsWithTagInCategoryInOrder($scope.tag, categoryCode, $scope.sort.code).then(function(response){
                    $scope.products = response;
                    $scope.size = response.length;
                    $scope.refreshing = '';
                });
      	    }

      	}
    }
});

productTag.controller('tagListCtrl', function($scope, $window, tagFactory) {
    $window.document.title = 'Tags | Noodle Monster';
	$scope.loading = true;
	tagFactory.getAllTags().then(function(response){
		$scope.tags = response;
		$scope.loading = false;
	});
});

productTag.factory('tagFactory', function($http, environment){

  var getAllTags = function(){
    return $http.get(environment.productUrl + '/tags').then(function(response){
      return response.data;
    });  	
  }

  return {
    getAllTags: getAllTags
  }
});

productTag.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/tags/:tag', {
        templateUrl: 'listing/tag.html',
        controller: 'tagCtrl'
      }).when('/tags', {
        templateUrl: 'listing/tag-list.html',
        controller: 'tagListCtrl'
      });
  }]);
