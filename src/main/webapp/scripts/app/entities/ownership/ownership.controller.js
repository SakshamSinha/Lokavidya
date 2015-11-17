'use strict';

angular.module('lokavidyaApp')
    .controller('OwnershipController', function ($scope, Ownership, OwnershipSearch) {
        $scope.ownerships = [];
        $scope.loadAll = function() {
            Ownership.query(function(result) {
               $scope.ownerships = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Ownership.get({id: id}, function(result) {
                $scope.ownership = result;
                $('#deleteOwnershipConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Ownership.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOwnershipConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            OwnershipSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.ownerships = result;
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
            $scope.ownership = {
                type: null,
                right: null,
                id: null
            };
        };
    });
