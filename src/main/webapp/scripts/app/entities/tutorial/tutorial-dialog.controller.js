'use strict';

angular.module('lokavidyaApp').controller('TutorialDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Tutorial', 'Project', 'User', 'Comment', 'ExtVideo', 'SegmentVideo', 'Ownership',
        function($scope, $stateParams, $modalInstance, $q, entity, Tutorial, Project, User, Comment, ExtVideo, SegmentVideo, Ownership) {

        $scope.tutorial = entity;
        $scope.projects = Project.query();
        $scope.users = User.query();
        $scope.projects = Project.query({filter: 'consists-is-null'});
        $q.all([$scope.consists.$promise, $scope.projects.$promise]).then(function() {
            if (!$scope.consists.project.id) {
                return $q.reject();
            }
            return Project.get({id : $scope.consists.project.id}).$promise;
        }).then(function(project) {
            $scope.projects.push(project);
        });
        $scope.comments = Comment.query();
        $scope.extvideos = ExtVideo.query({filter: 'extvideotutorial-is-null'});
        $q.all([$scope.extVideoTutorial.$promise, $scope.extvideos.$promise]).then(function() {
            if (!$scope.extVideoTutorial.extVideo.id) {
                return $q.reject();
            }
            return ExtVideo.get({id : $scope.extVideoTutorial.extVideo.id}).$promise;
        }).then(function(extVideo) {
            $scope.extvideos.push(extVideo);
        });
        $scope.segmentvideos = SegmentVideo.query();
        $scope.ownerships = Ownership.query();
        $scope.load = function(id) {
            Tutorial.get({id : id}, function(result) {
                $scope.tutorial = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:tutorialUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.tutorial.id != null) {
                Tutorial.update($scope.tutorial, onSaveFinished);
            } else {
                Tutorial.save($scope.tutorial, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
