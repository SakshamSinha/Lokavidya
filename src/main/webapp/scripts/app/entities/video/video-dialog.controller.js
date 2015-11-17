'use strict';

angular.module('lokavidyaApp').controller('VideoDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Video', 'Segment',
        function($scope, $stateParams, $modalInstance, $q, entity, Video, Segment) {

        $scope.video = entity;
        $scope.segments = Segment.query({filter: 'segmentvideo-is-null'});
        $q.all([$scope.segmentVideo.$promise, $scope.segments.$promise]).then(function() {
            if (!$scope.segmentVideo.segment.id) {
                return $q.reject();
            }
            return Segment.get({id : $scope.segmentVideo.segment.id}).$promise;
        }).then(function(segment) {
            $scope.segments.push(segment);
        });
        $scope.load = function(id) {
            Video.get({id : id}, function(result) {
                $scope.video = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:videoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.video.id != null) {
                Video.update($scope.video, onSaveFinished);
            } else {
                Video.save($scope.video, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
