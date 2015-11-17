'use strict';

angular.module('lokavidyaApp')
    .controller('OwnershipDetailController', function ($scope, $rootScope, $stateParams, entity, Ownership, Project, Tutorial, User) {
        $scope.ownership = entity;
        $scope.load = function (id) {
            Ownership.get({id: id}, function(result) {
                $scope.ownership = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:ownershipUpdate', function(event, result) {
            $scope.ownership = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
