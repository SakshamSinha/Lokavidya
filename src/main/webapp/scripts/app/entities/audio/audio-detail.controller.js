'use strict';

angular.module('lokavidyaApp')
    .controller('AudioDetailController', function ($scope, $rootScope, $stateParams, entity, Audio, Segment) {
        $scope.audio = entity;
        $scope.load = function (id) {
            Audio.get({id: id}, function(result) {
                $scope.audio = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:audioUpdate', function(event, result) {
            $scope.audio = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
