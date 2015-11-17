'use strict';

angular.module('lokavidyaApp')
    .controller('VideoDetailController', function ($scope, $rootScope, $stateParams, entity, Video, Segment) {
        $scope.video = entity;
        $scope.load = function (id) {
            Video.get({id: id}, function(result) {
                $scope.video = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:videoUpdate', function(event, result) {
            $scope.video = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
