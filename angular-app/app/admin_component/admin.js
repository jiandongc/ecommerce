var admin = angular.module('admin', ['ngRoute']);

admin.controller('adminOrderCtrl', function($window, $scope, adminFactory) {
  $window.document.title = 'Noodle Monster Admin | Order';
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

	var pushOrderEmail = function(type, orderNumber){
	    return $http.get(environment.orderUrl + '/admin/orders/email?orderNumber=' + orderNumber + '&type=' + type).then(function(response){
	        return response.data;
	    });
	};

	return {
    	getOrders: getOrders,
    	pushOrderEmail: pushOrderEmail
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
      });
}]);
