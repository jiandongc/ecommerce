var productTag = angular.module('productTag', ['ngRoute']);

productTag.controller('tagCtrl', function($scope, $routeParams, ngMeta, productFactory, environment) {
	$scope.loading = true;
	$scope.tag = $routeParams.tag;
	var title = '商品标签: ' + $scope.tag + ', 英国';
	var description = '来看看我们的[' + $scope.tag + ']商品，新用戶享受首單9折優惠，满39.99镑免邮。 GET FREE SHIPPING FOR ORDERS OVER £39.99!';
	ngMeta.setTitle(title);
	ngMeta.setTag('description', description);
	ngMeta.setTag('ogUrl', environment.tagPage + '/' + $scope.tag);
    ngMeta.setTag('ogTitle', title);
    ngMeta.setTag('ogDescription', description);
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

productTag.controller('tagListCtrl', function($scope, ngMeta, tagFactory) {
    ngMeta.setTitle('商品标签, Tags');
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
        templateUrl: 'listing_component/tag.html',
        controller: 'tagCtrl'
      }).when('/tags', {
        templateUrl: 'listing_component/tag-list.html',
        controller: 'tagListCtrl'
      });
  }]);
