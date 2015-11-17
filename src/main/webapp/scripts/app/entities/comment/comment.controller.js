'use strict';

angular.module('lokavidyaApp')
    .controller('CommentController', function ($scope, Comment, CommentSearch) {
        $scope.comments = [];
        $scope.loadAll = function() {
            Comment.query(function(result) {
               $scope.comments = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Comment.get({id: id}, function(result) {
                $scope.comment = result;
                $('#deleteCommentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Comment.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCommentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            CommentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.comments = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.comment = {
                content: null,
                id: null
            };
        };
    });
