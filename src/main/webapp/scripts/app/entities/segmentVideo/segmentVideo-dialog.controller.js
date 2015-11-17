'use strict';

angular.module('lokavidyaApp').controller('SegmentVideoDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'SegmentVideo', 'Segment', 'Tutorial', 'SegmentSequence',
        function($scope, $stateParams, $modalInstance, $q, entity, SegmentVideo, Segment, Tutorial, SegmentSequence) {

        $scope.segmentVideo = entity;
        $scope.segments = Segment.query();
        $scope.tutorials = Tutorial.query({filter: 'tutorialsvideo-is-null'});
        $q.all([$scope.tutorialSVideo.$promise, $scope.tutorials.$promise]).then(function() {
            if (!$scope.tutorialSVideo.tutorial.id) {
                return $q.reject();
            }
            return Tutorial.get({id : $scope.tutorialSVideo.tutorial.id}).$promise;
        }).then(function(tutorial) {
            $scope.tutorials.push(tutorial);
        });
        $scope.segmentsequences = SegmentSequence.query();
        $scope.load = function(id) {
            SegmentVideo.get({id : id}, function(result) {
                $scope.segmentVideo = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:segmentVideoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.segmentVideo.id != null) {
                SegmentVideo.update($scope.segmentVideo, onSaveFinished);
            } else {
                SegmentVideo.save($scope.segmentVideo, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
