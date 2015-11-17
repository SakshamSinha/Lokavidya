'use strict';

angular.module('lokavidyaApp')
    .controller('TagDetailController', function ($scope, $rootScope, $stateParams, entity, Tag, Project) {
        $scope.tag = entity;
        $scope.load = function (id) {
            Tag.get({id: id}, function(result) {
                $scope.tag = result;
            });
        };
        var unsubscribe = $rootScope.$on('lokavidyaApp:tagUpdate', function(event, result) {
            $scope.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
