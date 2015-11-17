'use strict';

angular.module('lokavidyaApp').controller('ProjectDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Project', 'User', 'Tag', 'Comment', 'Tutorial', 'Ownership',
        function($scope, $stateParams, $modalInstance, $q, entity, Project, User, Tag, Comment, Tutorial, Ownership) {

        $scope.project = entity;
        $scope.users = User.query();
        $scope.tags = Tag.query();
        $scope.comments = Comment.query();
        $scope.tutorials = Tutorial.query();
        $scope.ownerships = Ownership.query();
        $scope.load = function(id) {
            Project.get({id : id}, function(result) {
                $scope.project = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:projectUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.project.id != null) {
                Project.update($scope.project, onSaveFinished);
            } else {
                Project.save($scope.project, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
