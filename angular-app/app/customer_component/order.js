var order = angular.module('order',[]);

order.factory('orderFactory', function($http, environment){

	var createOrder = function(order) {
      return $http.post(environment.orderUrl + '/orders', order).then(function(response){
        return response.data;
      });
	};

	var addOrderStatus = function(orderNumber, orderStatus){
      return $http.post(environment.orderUrl + '/orders/' + orderNumber + '/status', orderStatus).then(function(response){
        return response.data;
      });
	};

	var getOrderByNumber = function(orderNumber){
      return $http.get(environment.orderUrl + '/orders/' + orderNumber).then(function(response){
        return response.data;
      });
	};

    var getOrderByCustomerId = function(customerId, status){
        return $http.get(environment.orderUrl + '/orders?customerId=' + customerId + '&status=' + status).then(function(response){
            return response.data;
        });
    }

    var downloadStripeClientSecret = function(orderNumber, stripeMetaData){
        return $http.post(environment.orderUrl + '/orders/stripe/' + orderNumber, stripeMetaData).then(function(response){
            return response.data;
        });
    };

    var addCustomerInfo = function(email, customerId) {
        var customerData = {id: customerId, email: email};
        return $http.post(environment.orderUrl + '/orders/customer', customerData).then(function(response) {
            return response.data;
        });
    };

	return {
    	createOrder: createOrder,
    	addOrderStatus: addOrderStatus,
    	getOrderByNumber: getOrderByNumber,
        getOrderByCustomerId: getOrderByCustomerId,
        downloadStripeClientSecret: downloadStripeClientSecret,
        addCustomerInfo: addCustomerInfo
  	}
});