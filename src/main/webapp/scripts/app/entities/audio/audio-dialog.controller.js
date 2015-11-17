'use strict';

angular.module('lokavidyaApp').controller('AudioDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Audio', 'Segment',
        function($scope, $stateParams, $modalInstance, $q, entity, Audio, Segment) {

        $scope.audio = entity;
        $scope.segments = Segment.query({filter: 'segmentaudio-is-null'});
        $q.all([$scope.segmentAudio.$promise, $scope.segments.$promise]).then(function() {
            if (!$scope.segmentAudio.segment.id) {
                return $q.reject();
            }
            return Segment.get({id : $scope.segmentAudio.segment.id}).$promise;
        }).then(function(segment) {
            $scope.segments.push(segment);
        });
        $scope.load = function(id) {
            Audio.get({id : id}, function(result) {
                $scope.audio = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:audioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.audio.id != null) {
                Audio.update($scope.audio, onSaveFinished);
            } else {
                Audio.save($scope.audio, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
