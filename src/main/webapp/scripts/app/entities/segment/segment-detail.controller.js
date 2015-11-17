'use strict';

angular.module('lokavidyaApp')
    .controller('SegmentDetailController', function ($scope, $rootScope, $stateParams, entity, Segment, SegmentVideo, Video, Image, Audio) {
        $scope.segment = entity;
        $scope.load = function (id) {
            Segment.get({id: id}, function(result) {
                $scope.segment = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:segmentUpdate', function(event, result) {
            $scope.segment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
