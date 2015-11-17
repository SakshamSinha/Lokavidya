'use strict';

angular.module('lokavidyaApp')
    .controller('CommentDetailController', function ($scope, $rootScope, $stateParams, entity, Comment, Project, Tutorial) {
        $scope.comment = entity;
        $scope.load = function (id) {
            Comment.get({id: id}, function(result) {
                $scope.comment = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:commentUpdate', function(event, result) {
            $scope.comment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
