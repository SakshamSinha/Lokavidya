'use strict';

angular.module('lokavidyaApp')
    .controller('SegmentController', function ($scope, Segment, SegmentSearch) {
        $scope.segments = [];
        $scope.loadAll = function() {
            Segment.query(function(result) {
               $scope.segments = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Segment.get({id: id}, function(result) {
                $scope.segment = result;
                $('#deleteSegmentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Segment.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSegmentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            SegmentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.segments = result;
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
            $scope.segment = {
                sync: false,
                id: null
            };
        };
    });
