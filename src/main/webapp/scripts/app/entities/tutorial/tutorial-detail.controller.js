'use strict';

angular.module('lokavidyaApp')
    .controller('TutorialDetailController', function ($scope, $rootScope, $stateParams, entity, Tutorial, Project, User, Comment, ExtVideo, SegmentVideo, Ownership) {
        $scope.tutorial = entity;
        $scope.load = function (id) {
            Tutorial.get({id: id}, function(result) {
                $scope.tutorial = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:tutorialUpdate', function(event, result) {
            $scope.tutorial = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
