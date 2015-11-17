'use strict';

angular.module('lokavidyaApp')
    .controller('ProjectDetailController', function ($scope, $rootScope, $stateParams, entity, Project, User, Tag, Comment, Tutorial, Ownership) {
        $scope.project = entity;
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
