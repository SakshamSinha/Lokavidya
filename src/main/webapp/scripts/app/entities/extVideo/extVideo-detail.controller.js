'use strict';

angular.module('lokavidyaApp')
    .controller('ExtVideoDetailController', function ($scope, $rootScope, $stateParams, entity, ExtVideo, Tutorial) {
        $scope.extVideo = entity;
        $scope.load = function (id) {
            ExtVideo.get({id: id}, function(result) {
                $scope.extVideo = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:extVideoUpdate', function(event, result) {
            $scope.extVideo = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
