var review = angular.module('review', ['ngRoute','ngResource']);

review.controller('feedbackReceivedCtrl', function($scope, $routeParams, reviewFactory) {
	reviewFactory.getReviewById($routeParams.feedbackId).then(function(response){
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
	$scope.voting = [];
	$scope.search;

	reviewFactory.getReviews('site', $scope.search, $scope.sort.code, $scope.offset, $scope.limit).then(function(response){
		$scope.loading = false;
		$scope.data = response;
		$scope.showLoadMoreButton = $scope.data.size > $scope.offset + $scope.limit;
		angular.forEach(response.comment, function(value, key) {
			$scope.voting.push(false);
		});
	});

	$scope.loadMore = function() {
		$scope.loadingMore = true;
		$scope.offset = $scope.offset + $scope.limit;
		reviewFactory.getReviews('site', $scope.search, $scope.sort.code, $scope.offset, $scope.limit).then(function(response){
			angular.forEach(response.comment, function(value, key) {
				$scope.data.comment.push(value);
				$scope.voting.push(false);
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
		$scope.voting = [];

		reviewFactory.getReviews('site', $scope.search, $scope.sort.code, $scope.offset, $scope.limit).then(function(response){
			$scope.loading = false;
			$scope.data = response;
			$scope.showLoadMoreButton = $scope.data.size > $scope.offset + $scope.limit;
			angular.forEach(response.comment, function(value, key) {
				$scope.voting.push(false);
			});
		});
	};

	$scope.filterFeedback = function(){
		$scope.loading = true;	
		$scope.loadingMore = false;
		$scope.showLoadMoreButton = false;
		$scope.offset = 0;
		$scope.limit = 10;
		$scope.voting = [];

		reviewFactory.getReviews('site', $scope.search, $scope.sort.code, $scope.offset, $scope.limit).then(function(response){
			$scope.loading = false;
			$scope.data = response;
			$scope.showLoadMoreButton = $scope.data.size > $scope.offset + $scope.limit;
			angular.forEach(response.comment, function(value, key) {
				$scope.voting.push(false);
			});
		});
	};

	$scope.voteUp = function(id, index){
		$scope.voting[index] = true;
		reviewFactory.voteUp(id).then(function(response){
			$scope.data.comment[index] = response;
			$scope.voting[index] = false;
		});
	};
});

review.factory('reviewFactory', function($http, environment){

	var addReview = function(comment, code){
        var configs = {headers: {'Content-Type' : 'application/json'}};
        var data = {'comment': comment, 'code': code};
        return $http.post(environment.reviewUrl + '/comments/', data, configs).then(function(response){
            return response.data;
        });
	};

	var getReviewById = function(id){
      return $http.get(environment.reviewUrl + '/comments/' + id).then(function(response){
        return response.data;
      });
	};

	var getReviews = function(code, search, sort, offset, limit){
		var params = '&offset=' + offset + '&limit=' + limit;

		if (sort) {
			params = params + '&sort=' + sort;
		}

		if (search) {
  			params = params + '&comment=' + search;
		}

      	return $http.get(environment.reviewUrl + '/comments?code=' + code + params).then(function(response){
        	return response.data;
      	});
	};

	var voteUp = function(id){
		return $http.post(environment.reviewUrl + '/comments/' + id + "/vote").then(function(response){
        	return response.data;
      	});
	}

    return {
         addReview: addReview,
         getReviewById: getReviewById,
         getReviews: getReviews,
         voteUp: voteUp
    };

});

review.component('postfeedback', {
  templateUrl: 'component/post-feedback.html',
  controller: function($scope, $location, reviewFactory){

  	reviewFactory.getReviews('site', undefined, undefined, 0, 1).then(function(response){
    	$scope.feedbackSize = '(' + response.size + ')';
  	});

    $scope.addReview = function(feedback){
    	if(typeof feedback === "undefined"){
    		return;
    	}

    	reviewFactory.addReview(feedback, 'site').then(function(response){
      		$location.path("/feedback-received/" + response.id);
    	});
  	}	
  },
  bindings: {seeallbutton: '<'}
});

review.config(
  function($routeProvider) {
    $routeProvider
    .when('/feedback-received/:feedbackId', {
    	templateUrl: 'review/feedback-received.html',
        controller: 'feedbackReceivedCtrl'})
    .when('/feedback', {
    	templateUrl: 'review/feedback.html',
        controller: 'allFeedbackCtrl'});
});
