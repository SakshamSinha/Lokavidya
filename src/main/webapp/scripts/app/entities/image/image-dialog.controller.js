'use strict';

angular.module('lokavidyaApp').controller('ImageDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Image', 'Segment',
        function($scope, $stateParams, $modalInstance, $q, entity, Image, Segment) {

        $scope.image = entity;
        $scope.segments = Segment.query({filter: 'segmentimage-is-null'});
        $q.all([$scope.segmentImage.$promise, $scope.segments.$promise]).then(function() {
            if (!$scope.segmentImage.segment.id) {
                return $q.reject();
            }
            return Segment.get({id : $scope.segmentImage.segment.id}).$promise;
        }).then(function(segment) {
            $scope.segments.push(segment);
        });
        $scope.load = function(id) {
            Image.get({id : id}, function(result) {
                $scope.image = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:imageUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.image.id != null) {
                Image.update($scope.image, onSaveFinished);
            } else {
                Image.save($scope.image, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
