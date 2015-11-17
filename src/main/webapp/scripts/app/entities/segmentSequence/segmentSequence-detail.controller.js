'use strict';

angular.module('lokavidyaApp')
    .controller('SegmentSequenceDetailController', function ($scope, $rootScope, $stateParams, entity, SegmentSequence, SegmentVideo) {
        $scope.segmentSequence = entity;
        $scope.load = function (id) {
            SegmentSequence.get({id: id}, function(result) {
                $scope.segmentSequence = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:segmentSequenceUpdate', function(event, result) {
            $scope.segmentSequence = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
