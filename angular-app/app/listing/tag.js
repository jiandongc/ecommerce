var productTag = angular.module('productTag', ['ngRoute']);

productTag.controller('tagsCtrl', function($scope, $routeParams, productFactory) {
	$scope.loading = true;
	$scope.tag = $routeParams.tag;
	$scope.code = $routeParams.code;
	$scope.sort = {display: "Our favourites", code: undefined};

	productFactory.getProductsWithTag($scope.code).then(function(response){
		$scope.products = response;
		$scope.loading = false;
	});

	$scope.sortby = function(display, sortcode){
		$scope.loading = true;
    	$scope.sort = {display: display, code: sortcode};
    	productFactory.getProductsWithTagInOrder($scope.code, sortcode).then(function(response){
			$scope.products = response;
			$scope.loading = false;
		});
  	}

});

productTag.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/tags/:code/:tag', {
        templateUrl: 'listing/tags.html',
        controller: 'tagsCtrl'
      });
  }]);
