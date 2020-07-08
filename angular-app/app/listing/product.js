var productDetail = angular.module('productDetail', ['ngRoute']);

productDetail.controller('productDetailCtrl', function($scope, $rootScope, $localstorage, $routeParams, $timeout, productFactory, shoppingCartFactory, customerFactory, categoryFactory) {
    $scope.addingItem = false;
    $scope.selectOptionAlert = false;
    $scope.addingItemToFavourites = false;
    $scope.addedToFavouriteAlert = false;
    $scope.loading = true;
    $scope.customerId = $localstorage.get("customer_id");

    productFactory.getProductWithCode($routeParams.code).then(function(response) {
        $scope.product = response;
        $scope.loading = false;

        categoryFactory.getCategoryData(response.categoryCode).then(function(response) {
            $scope.parentcategories = response.parents;
        });

        $scope.price = response.price;
        $scope.originalPrice = response.originalPrice;
        $scope.onSale = response.onSale;
        $scope.discountRate = response.discountRate;

        if ($scope.product.variants.length == 1) {
            var variant = $scope.product.variants[0];
            $scope.sku = variant.sku;
            $scope.description = variant.description;
            $scope.qty = variant.qty;
        } else {
            $scope.sku = undefined;
            $scope.description = undefined;
            $scope.qty = undefined;
        }

        $scope.selected = {};
        for (var attribute in $scope.product.attributes) {
            $scope.selected[attribute] = '';
        }
    });

    productFactory.getProductWithColorVariants($routeParams.code).then(function(response) {
        $scope.colorVariant = response;
    });

    $scope.select = function(attribute, value) {
        $scope.selected[attribute] = value;
        var variant = $scope.find();

        if (variant) {
            $scope.price = variant.price;
            $scope.originalPrice = variant.originalPrice;
            $scope.onSale = variant.isOnSale;
            $scope.discountRate = variant.discountRate;
            $scope.sku = variant.sku;
            $scope.description = variant.description;
            $scope.qty = variant.qty;
            $scope.selectOptionAlert = false;
        } else {
            $scope.price = $scope.product.price;
            $scope.originalPrice = $scope.product.originalPrice;
            $scope.onSale = $scope.product.onSale;
            $scope.discountRate = $scope.product.discountRate;
            $scope.sku = undefined;
            $scope.description = undefined;
            $scope.qty = undefined;
            $scope.selectOptionAlert = true;
        }
    };

    $scope.find = function() {
        outter: for (var i in $scope.product.variants) {
            var variant = $scope.product.variants[i];
            for (var attribute in $scope.selected) {
                if (variant[attribute] != $scope.selected[attribute]) {
                    continue outter;
                }
            }
            return variant;
        }
        return null;
    };

    $scope.addItemToCart = function(product) {

        if (typeof $scope.sku === "undefined" || typeof $scope.price === "undefined") {
            $scope.selectOptionAlert = true;
            return;
        }

        var imageUrl = product.images[0];
        $scope.addingItem = true;
        if (!$localstorage.containsKey("cart_uid")) {
            if ($localstorage.containsKey("customer_id")) {
                customerFactory.getCustomerById($localstorage.get("customer_id")).then(function(customerData) {
                    shoppingCartFactory.createShoppingCartForCustomer(customerData).then(function(cartUid) {
                        shoppingCartFactory.addItemToShoppingCart(product.name, product.code, $scope.price, imageUrl, $scope.sku, $scope.description, cartUid).then(function(data) {
                            $localstorage.set('cart_uid', cartUid);
                            $rootScope.$broadcast('updateCartSummary', true);
                            $scope.addingItem = false;
                        });
                    });

                });
            } else {
                shoppingCartFactory.createShoppingCartForGuest().then(function(cartUid) {
                    shoppingCartFactory.addItemToShoppingCart(product.name, product.code, $scope.price, imageUrl, $scope.sku, $scope.description, cartUid).then(function(data) {
                        $localstorage.set('cart_uid', cartUid);
                        $rootScope.$broadcast('updateCartSummary', true);
                        $scope.addingItem = false;
                    });
                });
            }
        } else {
            var cartUid = $localstorage.get('cart_uid', undefined);
            shoppingCartFactory.addItemToShoppingCart(product.name, product.code, $scope.price, imageUrl, $scope.sku, $scope.description, cartUid).then(function(data) {
                $rootScope.$broadcast('updateCartSummary', true);
                $scope.addingItem = false;
            });
        }

    };

    $scope.addToFavourite = function(product) {
        $scope.addingItemToFavourites = true;
        customerFactory.addToFavourite($scope.customerId, product.code).then(function(product) {
            $scope.addingItemToFavourites = false;
            $scope.addedToFavouriteAlert = true;
        });
    }
});

productDetail.factory('productFactory', function($http, environment) {
    var getProductWithCode = function(productCode) {
        return $http.get(environment.productUrl + '/products/' + productCode).then(function(response) {
            return response.data;
        });
    }

    var getProductWithColorVariants = function(productCode) {
        return $http.get(environment.productUrl + '/products/color/' + productCode).then(function(response) {
            return response.data;
            $scope.colorVariant = response.data;
        });
    }

    var getProductsWithTag = function(tag) {
        return $http.get(environment.productUrl + '/products?tg=' + tag).then(function(response) {
            return response.data;
        });
    }

    var getProductsWithTagInCategory = function(tag, categoryCode) {
        return $http.get(environment.productUrl + '/products?tg=' + tag + '&cc=' + categoryCode).then(function(response) {
            return response.data;
        });
    }

    var getProductsWithTagInOrder = function(tag, sort) {
        return $http.get(environment.productUrl + '/products?tg=' + tag + '&sort=' + sort).then(function(response) {
            return response.data;
        });
    }

    var getProductsWithTagInCategoryInOrder = function(tag, categoryCode, sort) {
        return $http.get(environment.productUrl + '/products?tg=' + tag + '&cc=' + categoryCode + '&sort=' + sort).then(function(response) {
            return response.data;
        });
    }

    var getProductsWithBrand = function(brand) {
        return $http.get(environment.productUrl + '/products?br=' + brand).then(function(response) {
            return response.data;
        });
    }

    var getProductsWithBrandInOrder = function(brand, sort) {
        return $http.get(environment.productUrl + '/products?br=' + brand + '&sort=' + sort).then(function(response) {
            return response.data;
        });
    }


    return {
        getProductWithCode: getProductWithCode,
        getProductWithColorVariants: getProductWithColorVariants,
        getProductsWithTag: getProductsWithTag,
        getProductsWithTagInCategory: getProductsWithTagInCategory,
        getProductsWithTagInOrder: getProductsWithTagInOrder,
        getProductsWithTagInCategoryInOrder: getProductsWithTagInCategoryInOrder,
        getProductsWithBrand: getProductsWithBrand,
        getProductsWithBrandInOrder: getProductsWithBrandInOrder
    }
});

productDetail.directive('onFinishInitSlider', function($timeout) {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function() {
                    $('.flexslider').flexslider({
                        animation: 'slide',
                        controlNav: 'thumbnails',
                        slideshow: false
                    });
                });
            }
        }
    }
});

productDetail.directive('dynamic', function($compile) {
    return {
        restrict: 'A',
        replace: true,
        link: function(scope, ele, attrs) {
            scope.$watch(attrs.dynamic, function(html) {
                ele.html(html);
                $compile(ele.contents())(scope);
            });
        }
    };
});

productDetail.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
        when('/products/:code', {
            templateUrl: 'listing/product.html',
            controller: 'productDetailCtrl'
        });
    }
]);