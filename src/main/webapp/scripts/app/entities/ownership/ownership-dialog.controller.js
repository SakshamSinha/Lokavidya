'use strict';

angular.module('lokavidyaApp').controller('OwnershipDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Ownership', 'Project', 'Tutorial', 'User',
        function($scope, $stateParams, $modalInstance, entity, Ownership, Project, Tutorial, User) {

        $scope.ownership = entity;
        $scope.projects = Project.query();
        $scope.tutorials = Tutorial.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Ownership.get({id : id}, function(result) {
                $scope.ownership = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('lokavidyaApp:ownershipUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ownership.id != null) {
                Ownership.update($scope.ownership, onSaveFinished);
            } else {
                Ownership.save($scope.ownership, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
