'use strict';

angular.module('lokavidyaApp')
    .controller('SegmentVideoController', function ($scope, SegmentVideo, SegmentVideoSearch) {
        $scope.segmentVideos = [];
        $scope.loadAll = function() {
            SegmentVideo.query(function(result) {
               $scope.segmentVideos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SegmentVideo.get({id: id}, function(result) {
                $scope.segmentVideo = result;
                $('#deleteSegmentVideoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SegmentVideo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSegmentVideoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            SegmentVideoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.segmentVideos = result;
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
            $scope.segmentVideo = {
                sync: false,
                id: null
            };
        };
    });
