'use strict';

angular.module('lokavidyaApp').controller('SegmentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Segment', 'SegmentVideo', 'Video', 'Image', 'Audio',
        function($scope, $stateParams, $modalInstance, entity, Segment, SegmentVideo, Video, Image, Audio) {

        $scope.segment = entity;
        $scope.segmentvideos = SegmentVideo.query();
        $scope.videos = Video.query();
        $scope.images = Image.query();
        $scope.audios = Audio.query();
        $scope.load = function(id) {
            Segment.get({id : id}, function(result) {
                $scope.segment = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:segmentUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.segment.id != null) {
                Segment.update($scope.segment, onSaveFinished);
            } else {
                Segment.save($scope.segment, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
