var productTag = angular.module('productTag', ['ngRoute']);

productTag.controller('tagCtrl', function($scope, $routeParams, productFactory) {
	$scope.loading = true;
	$scope.tag = $routeParams.tag;
	$scope.sort = {display: "Our favourites", code: undefined};

	productFactory.getProductsWithTag($scope.tag).then(function(response){
		$scope.products = response;
		$scope.size = response.length;
		$scope.loading = false;
	});

	$scope.sortby = function(display, sortcode){
		$scope.refreshing = 'opacity-05';
    	$scope.sort = {display: display, code: sortcode};
    	productFactory.getProductsWithTagInOrder($scope.tag, sortcode).then(function(response){
			$scope.products = response;
			$scope.size = response.length;
			$scope.refreshing = '';
		});
  	}

    $scope.refresh = function(categoryCode){
    	$scope.refreshing = 'opacity-05';
      	if(categoryCode == undefined){
        	productFactory.getProductsWithTag($scope.tag).then(function(response){
				$scope.products = response;
				$scope.size = response.length;
				$scope.refreshing = '';
          	});
      	} else {
          	productFactory.getProductsWithTagInCategory($scope.tag, categoryCode).then(function(response){
				$scope.products = response;
				$scope.size = response.length;
				$scope.refreshing = '';
          	});
      	}
    }
});

productTag.controller('tagListCtrl', function($scope, tagFactory) {
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
