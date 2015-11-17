'use strict';

angular.module('lokavidyaApp')
    .controller('TagController', function ($scope, Tag, TagSearch) {
        $scope.tags = [];
        $scope.loadAll = function() {
            Tag.query(function(result) {
               $scope.tags = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Tag.get({id: id}, function(result) {
                $scope.tag = result;
                $('#deleteTagConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Tag.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTagConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TagSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.tags = result;
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
            $scope.tag = {
                name: null,
                description: null,
                id: null
            };
        };
    });
