'use strict';

angular.module('lokavidyaApp')
    .controller('ImageDetailController', function ($scope, $rootScope, $stateParams, entity, Image, Segment) {
        $scope.image = entity;
        $scope.load = function (id) {
            Image.get({id: id}, function(result) {
                $scope.image = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:imageUpdate', function(event, result) {
            $scope.image = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
