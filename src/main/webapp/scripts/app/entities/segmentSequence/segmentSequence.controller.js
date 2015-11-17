'use strict';

angular.module('lokavidyaApp')
    .controller('SegmentSequenceController', function ($scope, SegmentSequence, SegmentSequenceSearch) {
        $scope.segmentSequences = [];
        $scope.loadAll = function() {
            SegmentSequence.query(function(result) {
               $scope.segmentSequences = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SegmentSequence.get({id: id}, function(result) {
                $scope.segmentSequence = result;
                $('#deleteSegmentSequenceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SegmentSequence.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSegmentSequenceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            SegmentSequenceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.segmentSequences = result;
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
            $scope.segmentSequence = {
                segmentsequence: null,
                id: null
            };
        };
    });
