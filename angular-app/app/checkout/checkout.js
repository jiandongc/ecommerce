var checkout = angular.module('checkout',[]);

checkout.controller('shippingCtrl', function($scope, $location, $localstorage, customerFactory) {
	var customerId = $localstorage.get("customer_id");

	customerFactory.getAddressesById(customerId).then(function(response){
        $scope.addresses = response;
		$scope.selected = 0;
        angular.forEach($scope.addresses, function(address, index){
      		if(address.defaultAddress){   		
				$scope.address = address;
      		}
    	});
    });

	$scope.select=function(index, address){
		$scope.selected=index;
		$scope.address=address;
	};

	$scope.continue = function(){
		$location.path("/delivery");
	}
});

checkout.controller('deliveryCtrl', function($scope, $location) {
	$scope.deliverOptions = [
		{
			id : 1,
			description : "Standard Delivery",
			info : "Receive it by Monday 3 July",
			cost : 2.75,
			primary : true
		}, {
			id : 2,
			description : "Tracked Express Delivery",
			info : "Receive it by Friday 30 June",
			cost : 6.95,
			primary : false
		}, {
			id : 3,
			description : "Saturday Delivery",
			info : "Receive it by Saturday 1 July",
			cost : 10.95,
			primary : false
		}
	];

	angular.forEach($scope.deliverOptions, function(deliverOption, index){
      if(deliverOption.primary){
		$scope.selected=index;      		
		$scope.deliverOption=deliverOption;
      }
    });

    $scope.select=function(index, deliverOption){
		$scope.selected=index;
		$scope.deliverOption=deliverOption;
	};

	$scope.continue = function(){
		$location.path("/billing");
	}
});

checkout.controller('billingCtrl', function($scope, $location) {
	$scope.sameAsShipping = true;
	$scope.addresses = [
		{
			id : 1,
			fullname : "Jiandong Chen",
			address1 : "22 Brampton House",
			address2 : "17 Albatross Way",
			city : "London",
			postcode : "SE16 7EB",
			country : "United Kingdom",
			phone : "07745324432",
			primary : false
		}, {
			id : 2,
			fullname : "Yujie Sun",
			address1 : "22 Brampton House, 17 Albatross Way",
			city : "London",
			postcode : "SE16 7EB",
			country : "United Kingdom",
			phone : "07745324432",
			primary : true
		}, {
			id : 3,
			fullname : "Joe Smith",
			address1 : "1 St Mary at Hill",
			city : "London",
			postcode : "EC3M 1BU",
			country : "United Kingdom",
			phone : "02072610002",
			primary : false
		}
	];

	$scope.select=function(index, address){
		console.log(index);
		console.log(address);
		$scope.selected=index;
		$scope.address=address;
	};

	$scope.check=function(){
    	if($scope.sameAsShipping){
    		$scope.sameAsShipping = false;
    	} else {
    		$scope.sameAsShipping = true;
    	}
	};

	$scope.continue = function(){
		$location.path("/payment");
	}
});

checkout.controller('paymentCtrl', function($scope, $location) {
	$scope.onlyNumbers = /^\d+$/;
    $scope.select=function(option){
    	$scope.option = option;
	};
});

checkout.config(
  function($routeProvider) {
    $routeProvider
    .when('/shipping', {
    	templateUrl: 'checkout/shipping.html',
        controller: 'shippingCtrl'})
    .when('/delivery', {
    	templateUrl: 'checkout/delivery.html',
        controller: 'deliveryCtrl'})
    .when('/billing', {
    	templateUrl: 'checkout/billing.html',
        controller: 'billingCtrl'})
    .when('/payment', {
    	templateUrl: 'checkout/payment.html',
        controller: 'paymentCtrl'});
});