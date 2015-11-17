'use strict';

angular.module('lokavidyaApp')
    .controller('ExtVideoController', function ($scope, ExtVideo, ExtVideoSearch) {
        $scope.extVideos = [];
        $scope.loadAll = function() {
            ExtVideo.query(function(result) {
               $scope.extVideos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ExtVideo.get({id: id}, function(result) {
                $scope.extVideo = result;
                $('#deleteExtVideoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ExtVideo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExtVideoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ExtVideoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.extVideos = result;
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
            $scope.extVideo = {
                title: null,
                url: null,
                isYoutube: false,
                id: null
            };
        };
    });
