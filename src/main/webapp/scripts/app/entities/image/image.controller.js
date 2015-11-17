'use strict';

angular.module('lokavidyaApp')
    .controller('ImageController', function ($scope, Image, ImageSearch) {
        $scope.images = [];
        $scope.loadAll = function() {
            Image.query(function(result) {
               $scope.images = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Image.get({id: id}, function(result) {
                $scope.image = result;
                $('#deleteImageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Image.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteImageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ImageSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.images = result;
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
            $scope.image = {
                name: null,
                caption: null,
                sync: false,
                id: null
            };
        };
    });
