var app = angular.module('store', [
    'ngRoute',
    'ngCookies',
    'home',
    'productDetail',
    'category',
    'customer',
    'cart',
    'checkout',
    'auth',
    'config',
    'order',
    'productTag',
    'review',
    'brand'
]);

app.controller('appCtrl', function($scope, $location, $localstorage, $rootScope, $q, environment, shoppingCartFactory, categoryFactory) {

    $scope.template = {
        header: "default-header.html",
        footer: "default-footer.html"
    };

    $scope.$watch(function() {
        return $localstorage.get('current_user');
    }, function(newValue, oldValue) {
        if (typeof $scope.currentUser === "undefined" || newValue !== oldValue) {
            $scope.currentUser = newValue;
        }
    });

    $scope.$watch(function() {
        return $localstorage.get('customer_id');
    }, function(newValue, oldValue) {
        if (typeof $scope.customerId === "undefined" || newValue !== oldValue) {
            $scope.customerId = newValue;
        }
    });

    $scope.$watch(function() {
        return $localstorage.get('cart_uid');
    }, function(newValue, oldValue) {
        if (typeof $scope.cartUid === "undefined" || newValue !== oldValue) {
            $scope.cartUid = newValue;
            if ($localstorage.get('cart_uid') !== false) {
                $rootScope.$broadcast('updateCartSummary', false);
            }
        }
    });

    $scope.$watch(function() {
        return $scope.categories;
    }, function(newValue, oldValue) {
        if (typeof $scope.categories === "undefined") {
            $scope.categories = [];
            var promises = [];
            for (i = 0; i < environment.categories.length; i = i + 1) {
                promises.push(categoryFactory.getSubCategories(environment.categories[i], 2));
            }

            $q.all(promises).then(function(response) {
                for (var i = 0; i < response.length; i++) {
                    $scope.categories.push(response[i]);;
                }
            })
        }
    });

    $scope.$on('updateCartSummary', function(event, showDropDown) {
        shoppingCartFactory.getShoppingCart($localstorage.get('cart_uid')).then(function(data) {
            $scope.cartUid = data.cartUid;
            $scope.totalQuantity = (data.quantity != 0 ? data.quantity : null);
            $scope.itemsTotal = (data.itemsTotal != 0 ? data.itemsTotal : null);
            $scope.cartItems = data.cartItems;
            if (showDropDown === true) {
                var dropdown = $('ul.menu li.dropdown').find('.dropdown-menu');
                dropdown.stop(true, true).fadeIn(1000, "swing", function() {
                    dropdown.stop(true, true).delay(5000).fadeOut(3800);
                });
            }
        }, function(error) {
            $scope.cartUid = null;
            $scope.totalQuantity = null;
            $scope.itemsTotal = null;
            $scope.cartItems = null;
        });
    });

    $scope.$on('resetUserInfo', function(event, args) {
        $localstorage.remove("access_token");
        $localstorage.remove("current_user");
        $localstorage.remove("customer_id");
    });

    $scope.$on('resetCartInfo', function(event, args) {
        $localstorage.remove("cart_uid");
        $scope.cartUid = null;
        $scope.totalQuantity = null;
        $scope.itemsTotal = null;
        $scope.cartItems = null;
    });

    $scope.$on('$routeChangeStart', function($event, next, current) {
        $scope.template.header = 'default-header.html';
        $scope.template.footer = 'default-footer.html';
    });

    $scope.removeItem = function(cartItem) {
        shoppingCartFactory.deleteItemFromShoppingCart($scope.cartUid, cartItem.sku).then(function(response) {
            $rootScope.$broadcast('updateCartSummary', false);
        });
    }
});

app.factory('$localstorage', ['$window', function($window) {
    return {
        set: function(key, value) {
            $window.localStorage[key] = value;
        },
        get: function(key, defaultValue) {
            return $window.localStorage[key] || defaultValue || false;
        },
        setObject: function(key, value) {
            $window.localStorage[key] = JSON.stringify(value);
        },
        getObject: function(key, defaultValue) {
            if ($window.localStorage[key] != undefined) {
                return JSON.parse($window.localStorage[key]);
            } else {
                return defaultValue || false;
            }
        },
        remove: function(key) {
            $window.localStorage.removeItem(key);
        },
        clear: function() {
            $window.localStorage.clear();
        },
        containsKey: function(key) {
            return $window.localStorage[key] != undefined;
        }
    }
}]);

app.factory('accessTokenInterceptor', function($localstorage, $location, $q, $rootScope, authFactory, $injector) {
    var service = this;

    service.request = function(config) {
        if (config.url.includes('/guesttoken')) {
            return config;
        }

        if ($localstorage.containsKey('access_token')) {
            config.headers.Authentication = $localstorage.get('access_token');
        } else {
            authFactory.downloadGuestToken().then(function(response) {
                $localstorage.set('access_token', response.headers("Authentication"));
            });
        }

        return config;

    };

    service.responseError = function(response) {
        if (response.status === 403) {
            if (response.config.headers.Authentication == undefined) {
                var $http = $injector.get('$http');
                return $http(response.config);
            } else {
                $rootScope.$broadcast('resetUserInfo');
                $location.path("/login");
            }
        }
        return $q.reject(response);
    };

    return service;
});

app.config(function($routeProvider, $httpProvider) {
    $routeProvider.otherwise({
        redirectTo: '/home'
    });
    $httpProvider.interceptors.push('accessTokenInterceptor');
});