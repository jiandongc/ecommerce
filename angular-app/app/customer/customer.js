var customer = angular.module('customer', ['ngRoute','ngResource']);

customer.controller('loginCtrl', function($scope, authService, $rootScope) {

    authService.assignGuestToken();
    $rootScope.loginError = false;

    $scope.login = function(credentials){
        authService.authenticateUser(credentials);
    };
});

customer.controller('registerCtrl', function($scope, authService, customerFactory) {

    authService.assignGuestToken();
    $scope.error = false;
    $scope.errorMsg = "";

    $scope.registerCustomer = function(){
        var newCustomer = {
            name : $scope.customer.name,
            title: $scope.customer.title,
            email : $scope.customer.email,
            mobile : $scope.customer.mobile,
            password : $scope.customer.password
        };

        customerFactory.save(newCustomer).then(function(data){
            $scope.error = false;
            var credentials = {
                email : data.email,
                password : newCustomer.password
            };
            authService.authenticateUser(credentials);
        }, function(error){
            $scope.error = true;
            if(error.status === 409){
                $scope.errorMsg = "This the email address is already used.";
            } else {
                $scope.errorMsg = "Technical error, please contact site admin: jiandong.c@gmail.com";
            }
        });
    };
});

customer.controller('accountCtrl', function($scope, $routeParams, customerFactory){
    customerFactory.getCustomerById($routeParams.id).then(function(response){
        $scope.customer = response;
    });
});

customer.controller('profileEditCtrl', function($scope, $routeParams, $location, $localstorage, customerFactory){
    customerFactory.getCustomerById($routeParams.id).then(function(response){
        $scope.customer = response;
    });

    $scope.updateProfile = function(){
        customerFactory.update($scope.customer).then(function(data){
            $scope.error = false;
            $localstorage.set('current_user', data.name);
            $localstorage.set('customer_id', data.id);
            $location.path("/account/" + data.id);
        }, function(error){
            $scope.error = true;
            if(error.status === 409){
                $scope.errorMsg = "This the email address is already used.";
            } else {
                $scope.errorMsg = "Technical error, please contact site admin: jiandong.c@gmail.com";
            }
        });
    };
});

customer.controller('addressBookCtrl', function($scope, $routeParams, $location, customerFactory){
    customerFactory.getAddressesById($routeParams.id).then(function(response){
        console.log(response);
        $scope.addresses = response;
    });
});

customer.controller('addAddressCtrl', function($scope, $routeParams, $location, customerFactory){
    customerFactory.getCustomerById($routeParams.id).then(function(response){
        $scope.address.title = response.title;
        $scope.address.name = response.name;
        $scope.address.mobile = response.mobile;
        $scope.address.defaultAddress = true;
        $scope.address.country = 'United Kingdom';
        $scope.customer = response;
    });

    $scope.addAddress = function(address){
        customerFactory.addAddress($scope.customer.id, address).then(function(data){
            $scope.error = false;
            $location.path("/account/" + $scope.customer.id + "/address-book");
        }, function(error){
            $scope.error = true;
            $scope.errorMsg = error;
        })
    }
});

customer.factory('customerFactory', function($http, environment){

    var getCustomerByEmail = function(credentials){
        return $http.get(environment.customerUrl + '/customers?email=' + credentials.email).then(function(response){
            return response.data;
        });
    };

    var getCustomerById = function(id){
        return $http.get(environment.customerUrl + '/customers/' + id).then(function(response){
            return response.data;
        });
    };

    var save = function(customer){
        var configs = {headers: {'Content-Type' : 'application/json'}};
        return $http.post(environment.customerUrl + '/customers', customer, configs).then(function(response){
            return response.data;
        });
    };

    var update = function(customer){
        var configs = {headers: {'Content-Type' : 'application/json'}};
        return $http.put(environment.customerUrl + '/customers', customer, configs).then(function(response){
            return response.data;
        });
    };

    var getAddressesById = function(customerId){
        return $http.get(environment.customerUrl + '/customers/' + customerId + '/addresses').then(function(response){
            return response.data;
        });
    };

    var addAddress = function(customerId, address){
        var configs = {headers: {'Content-Type' : 'application/json'}};
        return $http.post(environment.customerUrl + '/customers/' + customerId + '/addresses', address, configs).then(function(response){
            return response.data;
        });
    };

    return {
        getCustomerByEmail: getCustomerByEmail,
        getCustomerById: getCustomerById,
        save: save,
        update: update,
        getAddressesById: getAddressesById,
        addAddress: addAddress
    };
});

customer.directive('passwordMatch', [function () {
    return {
        restrict: 'A',
        scope:true,
        require: 'ngModel',
        link: function (scope, elem , attrs,control) {
            var checker = function () {
                var e1 = scope.$eval(attrs.ngModel); 
                var e2 = scope.$eval(attrs.passwordMatch);
                return e1 == e2;
            };
            scope.$watch(checker, function (n) {
               control.$setValidity("unique", n);
           });
        }
    };
}]);

customer.config(
  function($routeProvider) {

    $routeProvider.
    when('/login', {
        templateUrl: 'customer/login.html',
        controller: 'loginCtrl'
    }).when('/register', {
        templateUrl: 'customer/register.html',
        controller: 'registerCtrl'
    }).when('/account/:id', {
        templateUrl: 'customer/profile.html',
        controller: 'accountCtrl'
    }).when('/account/:id/edit', {
        templateUrl: 'customer/profile-edit.html',
        controller: 'profileEditCtrl'
    }).when('/account/:id/change-password', {
        templateUrl: 'customer/password-change.html',
        controller: 'accountCtrl'
    }).when('/account/:id/address-book', {
        templateUrl: 'customer/address-book.html',
        controller: 'addressBookCtrl'
    }).when('/account/:id/add-address', {
        templateUrl: 'customer/add-address.html',
        controller: 'addAddressCtrl'
    }).when('/account/:id/orders', {
        templateUrl: 'customer/orders.html',
        controller: 'accountCtrl'
    });
});



