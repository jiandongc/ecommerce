var checkout = angular.module('checkout',[]);

checkout.component('summary', {
  templateUrl: 'checkout/summary.html',
  controller: function($scope, $localstorage, shoppingCartFactory){
  	shoppingCartFactory.getShoppingCart($localstorage.get('cart_uid')).then(function(response){
  		$scope.shoppingCartData = response;
  	});
  }
});

checkout.controller('shippingCtrl', function($scope, $location, $localstorage, $rootScope, customerFactory, shoppingCartFactory) {

	$scope.template.header = 'checkout-header.html';
	$scope.template.footer = 'default-footer.html';
	$scope.saveAddressLoading = false;

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

	$scope.saveAddress = function(address){
		$scope.saveAddressLoading = true;
		var cartUid = $localstorage.get('cart_uid');
		var addressData = {
			addressType: 'Shipping',
      		name: address.name, 
      		title: address.title,
      		mobile: address.mobile,
      		addressLine1: address.addressLine1,
      		addressLine2: address.addressLine2,
      		addressLine3: address.addressLine3,
      		city: address.city,
      		country: address.country,
      		postcode: address.postcode
    	};

		shoppingCartFactory.addAddress(cartUid, addressData).then(function(response){
            $rootScope.$broadcast('updateCartSummary', false);
            $scope.saveAddressLoading = false;
            $location.path("/delivery");
        });
	}
});

checkout.controller('billingCtrl', function($scope, $location, $localstorage, $rootScope, customerFactory, shoppingCartFactory) {

	$scope.template.header = 'checkout-header.html';
	$scope.template.footer = 'default-footer.html';
	$scope.saveAddressLoading = false;

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

	$scope.saveAddress = function(address){
		$scope.saveAddressLoading = true;
		var cartUid = $localstorage.get('cart_uid');
		var addressData = {
			addressType: 'Billing',
      		name: address.name, 
      		title: address.title,
      		mobile: address.mobile,
      		addressLine1: address.addressLine1,
      		addressLine2: address.addressLine2,
      		addressLine3: address.addressLine3,
      		city: address.city,
      		country: address.country,
      		postcode: address.postcode
    	};

		shoppingCartFactory.addAddress(cartUid, addressData).then(function(response){
            $rootScope.$broadcast('updateCartSummary', false);
            $scope.saveAddressLoading = false;
            $location.path("/payment");
        });
	}

});

checkout.controller('deliveryCtrl', function($scope, $location, $localstorage, $rootScope, shoppingCartFactory) {

	$scope.template.header = 'checkout-header.html';
	$scope.template.footer = 'default-footer.html';
	$scope.saveDeliveryOptionLoading = false;

    shoppingCartFactory.getDeliveryOption($localstorage.get('cart_uid')).then(function(response){
        $scope.deliverOptions = response;
    });

	$scope.saveDeliveryOption = function(deliverOption){
		$scope.saveDeliveryOptionLoading = true;
		var cartUid = $localstorage.get('cart_uid');
		shoppingCartFactory.addDeliveryOption(cartUid, deliverOption).then(function(response){
            $rootScope.$broadcast('updateCartSummary', false);
            $scope.saveDeliveryOptionLoading = false;
            $location.path("/billing");
        });
	};	

    $scope.select=function(index, deliverOption){
		$scope.selected=index;
		$scope.deliverOption=deliverOption;
	};
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