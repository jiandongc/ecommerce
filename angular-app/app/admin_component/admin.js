var admin = angular.module('admin', ['ngRoute']);

admin.controller('adminOrderCtrl', function(ngMeta, $scope, adminFactory) {
  ngMeta.setTitle('Order Admin');
  $scope.template.header = 'admin-dashboard-header.html';
  $scope.template.footer = 'default-footer.html';
  $scope.status = 'open';
  $scope.loading = true;
  $scope.error = false;

  adminFactory.getOrders('open', null, null).then(function(response) {
    $scope.loading = false;
    $scope.orders = response;
  });

  $scope.setStatus = function(status) {
    $scope.status = status;
  }

  $scope.applyFilter = function() {
      $scope.loading = true;
      $scope.error = false;
      adminFactory.getOrders($scope.status, $scope.orderDate, $scope.orderNumber).then(function(response) {
        $scope.loading = false;
        $scope.orders = response;
      }, function(error){
        $scope.loading = false;
        $scope.error = true;
        $scope.errorMsg = error;
      });
  }
});

admin.controller('adminOrderDetailsCtrl', function($window, $scope, $routeParams, adminFactory, orderFactory, $localstorage) {
  $window.document.title = 'Noodle Monster Admin | Order';
  $scope.template.header = 'admin-dashboard-header.html';
  $scope.template.footer = 'default-footer.html';
  $scope.status = 'Choose a new status';
  $scope.voucherCode = null;
  $scope.addingStatus = false;
  $scope.sendingEmail = false;

  orderFactory.getOrderByNumber($routeParams.orderNumber).then(function(response) {
    $scope.order = response;
  });

  $scope.setStatus = function(status) {
    $scope.status = status;
  }

  $scope.addStatus = function() {
    if($scope.status == 'Choose a new status'){
        return;
    }

    $scope.addingStatus = true;
    orderFactory.addOrderStatus($scope.order.orderNumber, {status: $scope.status, description: 'admin: ' + $localstorage.get('current_user')}).then(function(response){
        $scope.order = response;
        $scope.addingStatus = false;
    });

  }

  $scope.pushEmail = function(type) {
      $scope.sendingEmail = true;
      adminFactory.pushOrderEmail(type, $scope.order.orderNumber).then(function(response){
          $scope.sendingEmail = false;
      });
  }

  $scope.pushReviewRequestEmailWithVoucherCode = function(type) {
    if($scope.voucherCode == null){
        return;
    }

    $scope.sendingEmail = true;
    adminFactory.pushOrderEmail(type, $scope.order.orderNumber, $scope.voucherCode).then(function(response){
        $scope.sendingEmail = false;
    });
  }

  $scope.downloadInvoice = function() {
        html2canvas(document.getElementById('invoice'), {
            onrendered: function (canvas) {
                var data = canvas.toDataURL();
                var docDefinition = {
                    content: [{
                        image: data,
                        width: 500,
                    }]
                };
                pdfMake.createPdf(docDefinition).download("invoice.pdf");
            }
        });
  }

});

admin.controller('adminCartCtrl', function(ngMeta, $scope, adminFactory) {
  ngMeta.setTitle('Shopping Cart Admin | Shopping Cart');
  $scope.template.header = 'admin-dashboard-header.html';
  $scope.template.footer = 'default-footer.html';
  $scope.loading = true;
  $scope.error = false;

  $scope.applyFilter = function() {
      $scope.loading = true;
      $scope.error = false;
      adminFactory.getShoppingCart($scope.creationDate).then(function(response) {
        $scope.loading = false;
        $scope.carts = response;
      }, function(error){
        $scope.loading = false;
        $scope.error = true;
        $scope.errorMsg = error;
      });
  }

  $scope.getCurrentDate = function() {
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();
    return yyyy + '-' + mm + '-01';
  }

  $scope.creationDate = $scope.getCurrentDate();
  adminFactory.getShoppingCart($scope.creationDate).then(function(response) {
    $scope.loading = false;
    $scope.carts = response;
  });

});

admin.controller('adminCartDetailsCtrl', function($window, $scope, $routeParams, $location, adminFactory, shoppingCartFactory) {
  $window.document.title = 'Noodle Monster Admin | Shopping Cart';
  $scope.template.header = 'admin-dashboard-header.html';
  $scope.template.footer = 'default-footer.html';
  $scope.sendingEmail = false;

  shoppingCartFactory.getShoppingCart($routeParams.cartUid).then(function(response) {
    $scope.cart = response;
  });

  $scope.deleteShoppingCart = function(cartUid) {
      adminFactory.deleteShoppingCart(cartUid).then(function(response) {
        $location.path("/admin/carts");
      });
  }

  $scope.pushEmail = function(type) {
      $scope.sendingEmail = true;
      adminFactory.pushShoppingCartEmail(type, $scope.cart.cartUid).then(function(response){
          $scope.sendingEmail = false;
      });
  }

});

admin.factory('adminFactory', function($http, environment){

	var getOrders = function(status, orderDate, orderNumber) {
	    var url = '/admin/orders?sort=date.desc';

	    if (status != null & status != '') {
	        url = url + '&status=' + status;
	    }

	    if (orderDate != null && orderDate != '') {
	        url = url + '&date=' + orderDate;
	    }

	    if (orderNumber != null && orderNumber != '') {
	        url = url + '&orderNumber=' + orderNumber;
	    }

        return $http.get(environment.orderUrl + url).then(function(response){
            return response.data;
        });
	};

	var pushOrderEmail = function(type, orderNumber, voucherCode){
	    if (voucherCode != null){
	        return $http.get(environment.orderUrl + '/admin/orders/email?orderNumber=' + orderNumber + '&type=' + type + '&voucherCode=' + voucherCode).then(function(response){
	            return response.data;
	        });
	    } else {
            return $http.get(environment.orderUrl + '/admin/orders/email?orderNumber=' + orderNumber + '&type=' + type).then(function(response){
                return response.data;
            });
	    }

	};

	var pushShoppingCartEmail = function(type, cartUid){
	    return $http.get(environment.shoppingCartUrl + '/admin/carts/email?cartUid=' + cartUid + '&type=' + type).then(function(response){
	        return response.data;
	    });
	};

	var getShoppingCart = function(creationDate){
	    var url = '/admin/carts';

	    if(creationDate != null){
	        url = url + "?date=" + creationDate;
	    }
	    return $http.get(environment.shoppingCartUrl + url).then(function(response){
	        return response.data;
        });
	}

	var deleteShoppingCart = function(cartUid){
	    return $http.delete(environment.shoppingCartUrl + '/admin/carts/' + cartUid).then(function(response){
        	return response.data;
        });
	}

	return {
    	getOrders: getOrders,
    	pushOrderEmail: pushOrderEmail,
    	getShoppingCart: getShoppingCart,
    	deleteShoppingCart: deleteShoppingCart,
    	pushShoppingCartEmail: pushShoppingCartEmail
  	}
});

admin.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider
      .when('/admin/orders', {
        templateUrl: 'admin_component/order.html',
        controller: 'adminOrderCtrl'
      }).when('/admin/orders/:orderNumber', {
        templateUrl: 'admin_component/order-details.html',
        controller: 'adminOrderDetailsCtrl'
      }).when('/admin/carts', {
        templateUrl: 'admin_component/cart.html',
        controller: 'adminCartCtrl'
      }).when('/admin/carts/:cartUid', {
        templateUrl: 'admin_component/cart-details.html',
        controller: 'adminCartDetailsCtrl'
      });
}]);
