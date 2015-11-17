'use strict';

angular.module('lokavidyaApp')
    .controller('SegmentVideoDetailController', function ($scope, $rootScope, $stateParams, entity, SegmentVideo, Segment, Tutorial, SegmentSequence) {
        $scope.segmentVideo = entity;
        $scope.load = function (id) {
            SegmentVideo.get({id: id}, function(result) {
                $scope.segmentVideo = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:segmentVideoUpdate', function(event, result) {
            $scope.segmentVideo = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
