var app = angular.module('store', [
	'ngRoute',
	'allProduct',
	'productDetail',
	'customer',
	'ngCookies'
]);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      otherwise({
        redirectTo: '/products'
      });
  }]);


app.controller('appCtrl', function ($scope, $cookies, $location) {
	$scope.$watch(function(){return $cookies.currentUser;}, function(newValue, oldValue){
		if($scope.currentUser === undefined || newValue !== oldValue){
			$scope.currentUser = $cookies.currentUser;
		}
	});
	
	$scope.logout = function(){
		delete $cookies['currentUser'];
		delete $cookies['access_token'];
		$location.path("#");	
	}
});

