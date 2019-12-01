var review = angular.module('review', ['ngRoute','ngResource']);

review.controller('feedbackReceivedCtrl', function($scope, $routeParams, reviewFactory) {
	reviewFactory.getFeedbackById($routeParams.feedbackId).then(function(response){
		$scope.feedback = response;
	});
});

review.controller('allFeedbackCtrl', function($scope, reviewFactory) {
	$scope.loading = true;	
	$scope.loadingMore = false;
	$scope.showLoadMoreButton = false;
	$scope.offset = 0;
	$scope.limit = 10;
	$scope.sort = {display: "Newest First", code: 'date.desc'};
	$scope.search;

	reviewFactory.getFeedback($scope.search, $scope.sort.code, $scope.offset, $scope.limit).then(function(response){
		$scope.loading = false;
		$scope.data = response;
		$scope.showLoadMoreButton = $scope.data.size > $scope.offset + $scope.limit;
	});

	$scope.loadMore = function() {
		$scope.loadingMore = true;
		$scope.offset = $scope.offset + $scope.limit;
		reviewFactory.getFeedback($scope.search, $scope.sort.code, $scope.offset, $scope.limit).then(function(response){
			angular.forEach(response.feedback, function(value, key) {
				$scope.data.feedback.push(value);
			});
			$scope.loadingMore = false;
			$scope.showLoadMoreButton = $scope.data.size > $scope.offset + $scope.limit;
		});
	};

	$scope.sortBy = function(display, code) {
		$scope.loading = true;	
		$scope.loadingMore = false;
		$scope.showLoadMoreButton = false;
		$scope.offset = 0;
		$scope.limit = 10;
		$scope.sort = {display: display, code: code};

		reviewFactory.getFeedback($scope.search, $scope.sort.code, $scope.offset, $scope.limit).then(function(response){
			$scope.loading = false;
			$scope.data = response;
			$scope.showLoadMoreButton = $scope.data.size > $scope.offset + $scope.limit;
		});
	};

	$scope.filterFeedback = function(){
		$scope.loading = true;	
		$scope.loadingMore = false;
		$scope.showLoadMoreButton = false;
		$scope.offset = 0;
		$scope.limit = 10;

		reviewFactory.getFeedback($scope.search, $scope.sort.code, $scope.offset, $scope.limit).then(function(response){
			$scope.loading = false;
			$scope.data = response;
			$scope.showLoadMoreButton = $scope.data.size > $scope.offset + $scope.limit;
		});
	};
});

review.factory('reviewFactory', function($http, environment){

	var addFeedback = function(comment){
        var configs = {headers: {'Content-Type' : 'application/json'}};
        var data = {text: comment};
        return $http.post(environment.reviewUrl + '/reviews/', data, configs).then(function(response){
            return response.data;
        });
	};

	var getFeedbackById = function(id){
      return $http.get(environment.reviewUrl + '/reviews/' + id).then(function(response){
        return response.data;
      });
	};

	var getFeedback = function(search, sort, offset, limit){
		var params = 'offset=' + offset + '&limit=' + limit;

		if (sort) {
			params = params + '&sort=' + sort;
		}

		if (search) {
  			params = params + '&text=' + search;
		}

      	return $http.get(environment.reviewUrl + '/reviews?' + params).then(function(response){
        	return response.data;
      	});
	}

    return {
         addFeedback: addFeedback,
         getFeedbackById: getFeedbackById,
         getFeedback: getFeedback
    };

});

checkout.config(
  function($routeProvider) {
    $routeProvider
    .when('/feedback_received/:feedbackId', {
    	templateUrl: 'review/feedback_received.html',
        controller: 'feedbackReceivedCtrl'})
    .when('/feedback', {
    	templateUrl: 'review/feedback.html',
        controller: 'allFeedbackCtrl'});
});
