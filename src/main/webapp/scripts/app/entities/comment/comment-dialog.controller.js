'use strict';

angular.module('lokavidyaApp').controller('CommentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Comment', 'Project', 'Tutorial',
        function($scope, $stateParams, $modalInstance, entity, Comment, Project, Tutorial) {

        $scope.comment = entity;
        $scope.projects = Project.query();
        $scope.tutorials = Tutorial.query();
        $scope.load = function(id) {
            Comment.get({id : id}, function(result) {
                $scope.comment = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:commentUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.comment.id != null) {
                Comment.update($scope.comment, onSaveFinished);
            } else {
                Comment.save($scope.comment, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
