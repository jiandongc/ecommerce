var productDetail = angular.module('productDetail', ['ngRoute']);

productDetail.controller('productDetailCtrl', function($scope, $rootScope, $localstorage, $routeParams, $timeout, ngMeta, productFactory, shoppingCartFactory, customerFactory, categoryFactory, reviewFactory, environment) {
    $scope.addingItem = false;
    $scope.selectOptionAlert = false;
    $scope.addingItemToFavourites = false;
    $scope.addedToFavouriteAlert = false;
    $scope.loading = true;
    $scope.customerId = $localstorage.get("customer_id");
    $scope.display = 'itemInfo';

    productFactory.getProductWithCode($routeParams.code).then(function(response) {
        $scope.product = response;
        $scope.loading = false;

        categoryFactory.getCategoryData(response.categoryCode).then(function(response) {
            $scope.parentcategories = response.parents;
            $scope.breadcrumbJsonLd = {"@context": "https://schema.org", "@type": "BreadcrumbList", "itemListElement": [{"@type": "ListItem", "position": 1, "name": 'Home', "item": environment.homePage}]};
            $scope.parentcategories.forEach(function (category, i) {
                $scope.breadcrumbJsonLd.itemListElement.push({
                    "@type": "ListItem",
                    "position": i+2,
                    "name": category.name,
                    "item": environment.categoryPage + '/' + category.code
                });
            });
            $('#breadcrumb-json-ld').html(JSON.stringify($scope.breadcrumbJsonLd));
        });

        var variant = $scope.product.variants[0];
        $scope.sku = variant.sku;
        $scope.description = variant.description;
        $scope.qty = variant.qty;
        $scope.price = variant.price;
        $scope.originalPrice = variant.originalPrice;
        $scope.onSale = variant.isOnSale;
        $scope.saleEndDate = variant.saleEndDate;
        $scope.discountRate = variant.discountRate;

        if ($scope.product.variants.length > 1 || $scope.product.type == 'Combo') {
            $scope.sku = undefined;
            $scope.description = undefined;
        }

        $scope.selected = {};
        for (var attribute in $scope.product.attributes) {
            $scope.selected[attribute] = '';
        }

        var description = '愛吃的你千萬不要錯過[' + $scope.product.name + ']，新用戶享受首單9折優惠，满39.99镑免邮。 GET FREE SHIPPING FOR ORDERS OVER £39.99!';
        ngMeta.setTitle($scope.product.name + ', 英国');
        ngMeta.setTag('description', description);
        ngMeta.setTag('ogUrl', environment.productPage + '/' + $scope.product.code);
        ngMeta.setTag('ogType', 'og:product');
        ngMeta.setTag('ogTitle', $scope.product.name);
        ngMeta.setTag('ogDescription', $scope.product.shortDescription != null ? $scope.product.shortDescription : description);
        ngMeta.setTag('ogImage', $scope.product.images[0]);
        ngMeta.setTag('ogPriceAmount', $scope.price);
        ngMeta.setTag('ogPriceCurrency', 'GBP');

        $scope.productJsonLd = {
            "@context":"http://schema.org",
            "@type":"Product",
            "name": $scope.product.name,
            "image": $scope.product.images,
            "sku": $scope.sku,
            "description": $scope.shortDescription != null ? $scope.shortDescription: null,
            "brand": {
                "@type": "Brand",
                "name": $scope.product.brand != null ? $scope.product.brand.name : null
            },
            "offers": {
                "@type": "Offer",
                "availability": $scope.qty > 0 ? "http://schema.org/InStock" : "http://schema.org/OutOfStock",
                "price": $scope.price,
                "priceCurrency": "GBP",
                "url": environment.productPage + '/' + $routeParams.code,
                "priceValidUntil": "2021-12-31"
            }
        }
        $('#product-json-ld').html(JSON.stringify($scope.productJsonLd));

    });

    productFactory.getRelatedProducts('tier', $routeParams.code).then(function(response) {
        $scope.relatedProducts = response;
    });

    $scope.select = function(attribute, value) {
        $scope.selected[attribute] = value;
        if ($scope.product.type == 'Combo') {
            var sku = '';
            var description = '';
            for (var attribute in $scope.selected) {
                var selectedValue = $scope.selected[attribute];
                var values = $scope.product.attributes[attribute];
                var index = values.indexOf(selectedValue)
                if (index == -1) {
                    $scope.sku = undefined;
                    $scope.description = undefined;
                    return;
                } else {
                    sku = sku + index;
                    description = description + attribute + ": " + selectedValue + "; "
                }
            }
            $scope.sku = $scope.product.variants[0].sku + '-' + sku;
            $scope.description = description;
            $scope.selectOptionAlert = false;
        } else {
            var variant = $scope.find();

            if (variant) {
                $scope.price = variant.price;
                $scope.originalPrice = variant.originalPrice;
                $scope.onSale = variant.isOnSale;
                $scope.saleEndDate = variant.saleEndDate;
                $scope.discountRate = variant.discountRate;
                $scope.sku = variant.sku;
                $scope.description = variant.description;
                $scope.qty = variant.qty;
                $scope.selectOptionAlert = false;
            } else {
                $scope.price = $scope.product.price;
                $scope.originalPrice = $scope.product.originalPrice;
                $scope.onSale = $scope.product.onSale;
                $scope.saleEndDate = $scope.product.saleEndDate;
                $scope.discountRate = $scope.product.discountRate;
                $scope.sku = undefined;
                $scope.description = undefined;
                $scope.qty = undefined;
                $scope.selectOptionAlert = true;
            }
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
                        shoppingCartFactory.addItemToShoppingCart(product.name, product.code, $scope.price, imageUrl, $scope.sku, $scope.description, cartUid, product.vat).then(function(data) {
                            $localstorage.set('cart_uid', cartUid);
                            $rootScope.$broadcast('updateCartSummary', true);
                            $scope.addingItem = false;
                        });
                    });

                });
            } else {
                shoppingCartFactory.createShoppingCartForGuest().then(function(cartUid) {
                    shoppingCartFactory.addItemToShoppingCart(product.name, product.code, $scope.price, imageUrl, $scope.sku, $scope.description, cartUid, product.vat).then(function(data) {
                        $localstorage.set('cart_uid', cartUid);
                        $rootScope.$broadcast('updateCartSummary', true);
                        $scope.addingItem = false;
                    });
                });
            }
        } else {
            var cartUid = $localstorage.get('cart_uid', undefined);
            shoppingCartFactory.addItemToShoppingCart(product.name, product.code, $scope.price, imageUrl, $scope.sku, $scope.description, cartUid, product.vat).then(function(data) {
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

    $scope.toggle = function(value) {
        $scope.display = value;
    }

    $scope.loadingReviews = true;
    $scope.loadingMoreReviews = false;
    $scope.showLoadMoreButton = false;
    $scope.offset = 0;
    $scope.limit = 10;
    $scope.sort = {
        display: "Newest First",
        code: 'date.desc'
    };
    $scope.voting = [];

    reviewFactory.getReviews($routeParams.code, undefined, $scope.sort.code, $scope.offset, $scope.limit).then(function(response) {
        $scope.loadingReviews = false;
        $scope.reviews = response;
        $scope.showLoadMoreButton = $scope.reviews.size > $scope.offset + $scope.limit;
        angular.forEach(response.comment, function(value, key) {
            $scope.voting.push(false);
        });
    });

    $scope.loadMore = function() {
        $scope.loadingMoreReviews = true;
        $scope.offset = $scope.offset + $scope.limit;
        reviewFactory.getReviews($routeParams.code, undefined, $scope.sort.code, $scope.offset, $scope.limit).then(function(response) {
            angular.forEach(response.comment, function(value, key) {
                $scope.reviews.comment.push(value);
                $scope.voting.push(false);
            });
            $scope.loadingMoreReviews = false;
            $scope.showLoadMoreButton = $scope.reviews.size > $scope.offset + $scope.limit;
        });
    };

    $scope.sortBy = function(display, code) {
        $scope.loadingReviews = true;
        $scope.loadingMoreReviews = false;
        $scope.showLoadMoreButton = false;
        $scope.offset = 0;
        $scope.limit = 10;
        $scope.sort = {
            display: display,
            code: code
        };
        $scope.voting = [];

        reviewFactory.getReviews($routeParams.code, undefined, $scope.sort.code, $scope.offset, $scope.limit).then(function(response) {
            $scope.loadingReviews = false;
            $scope.reviews = response;
            $scope.showLoadMoreButton = $scope.reviews.size > $scope.offset + $scope.limit;
            angular.forEach(response.comment, function(value, key) {
                $scope.voting.push(false);
            });
        });
    };

    $scope.voteUp = function(id, index) {
        $scope.voting[index] = true;
        reviewFactory.voteUp(id).then(function(response) {
            $scope.reviews.comment[index] = response;
            $scope.voting[index] = false;
        });
    };

    $scope.setStockNotification = function(product, email) {
        if(typeof email === "undefined"){
            return;
        }

        $scope.addingBackInStockNotification = true;
        customerFactory.addToNotifyInStock(product.code, email).then(function(product) {
            $scope.addingBackInStockNotification = false;
            $scope.backInStockNotificationAddedAlter = true;
        });
    }
});

productDetail.controller('createProductReviewCtrl', function($scope, $routeParams, $location, productFactory, reviewFactory) {
    productFactory.getProductWithCode($routeParams.code).then(function(response) {
        $scope.product = response;
    });

    $scope.adding = false;
    $scope.addReview = function(review) {
        if(typeof review === "undefined"){
            return;
        }
        $scope.adding = true;
        reviewFactory.addReview(review, $routeParams.code).then(function(response){
            $scope.adding = false;
            $location.path("/products/" + $routeParams.code);
        });
    }
});

productDetail.factory('productFactory', function($http, environment) {
    var getProductWithCode = function(productCode) {
        return $http.get(environment.productUrl + '/products/' + productCode).then(function(response) {
            return response.data;
        });
    }

    var getRelatedProducts = function(type, productCode) {
        return $http.get(environment.productUrl + '/products/' + type + '/' + productCode).then(function(response) {
            return response.data;
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
        getRelatedProducts: getRelatedProducts,
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
        $routeProvider
        .when('/products/:code', {
            templateUrl: 'listing_component/product.html',
            controller: 'productDetailCtrl'
        })
        .when('/products/create-review/:code', {
             templateUrl: 'listing_component/create-product-review.html',
             controller: 'createProductReviewCtrl'
        });
    }
]);