'use strict';

angular.module('lokavidyaApp').controller('ExtVideoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ExtVideo', 'Tutorial',
        function($scope, $stateParams, $modalInstance, entity, ExtVideo, Tutorial) {

        $scope.extVideo = entity;
        $scope.tutorials = Tutorial.query();
        $scope.load = function(id) {
            ExtVideo.get({id : id}, function(result) {
                $scope.extVideo = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:extVideoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.extVideo.id != null) {
                ExtVideo.update($scope.extVideo, onSaveFinished);
            } else {
                ExtVideo.save($scope.extVideo, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
