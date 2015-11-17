'use strict';

angular.module('lokavidyaApp')
    .controller('TutorialController', function ($scope, Tutorial, TutorialSearch) {
        $scope.tutorials = [];
        $scope.loadAll = function() {
            Tutorial.query(function(result) {
               $scope.tutorials = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Tutorial.get({id: id}, function(result) {
                $scope.tutorial = result;
                $('#deleteTutorialConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Tutorial.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTutorialConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TutorialSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.tutorials = result;
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
            $scope.tutorial = {
                title: null,
                description: null,
                id: null
            };
        };
    });
