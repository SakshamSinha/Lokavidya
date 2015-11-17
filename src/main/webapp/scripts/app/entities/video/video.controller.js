'use strict';

angular.module('lokavidyaApp')
    .controller('VideoController', function ($scope, Video, VideoSearch) {
        $scope.videos = [];
        $scope.loadAll = function() {
            Video.query(function(result) {
               $scope.videos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Video.get({id: id}, function(result) {
                $scope.video = result;
                $('#deleteVideoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Video.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteVideoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            VideoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.videos = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.video = {
                name: null,
                sync: false,
                id: null
            };
        };
    });
