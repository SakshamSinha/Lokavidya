'use strict';

angular.module('lokavidyaApp').controller('SegmentSequenceDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'SegmentSequence', 'SegmentVideo',
        function($scope, $stateParams, $modalInstance, $q, entity, SegmentSequence, SegmentVideo) {

        $scope.segmentSequence = entity;
        $scope.segmentvideos = SegmentVideo.query({filter: 'segmentvideossequence-is-null'});
        $q.all([$scope.segmentVideoSSequence.$promise, $scope.segmentvideos.$promise]).then(function() {
            if (!$scope.segmentVideoSSequence.segmentVideo.id) {
                return $q.reject();
            }
            return SegmentVideo.get({id : $scope.segmentVideoSSequence.segmentVideo.id}).$promise;
        }).then(function(segmentVideo) {
            $scope.segmentvideos.push(segmentVideo);
        });
        $scope.load = function(id) {
            SegmentSequence.get({id : id}, function(result) {
                $scope.segmentSequence = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:segmentSequenceUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.segmentSequence.id != null) {
                SegmentSequence.update($scope.segmentSequence, onSaveFinished);
            } else {
                SegmentSequence.save($scope.segmentSequence, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
