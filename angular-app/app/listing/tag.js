var productTag = angular.module('productTag', ['ngRoute']);

productTag.controller('tagsCtrl', function($scope, $routeParams, productFactory) {
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

productTag.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/tags/:tag', {
        templateUrl: 'listing/tags.html',
        controller: 'tagsCtrl'
      });
  }]);
