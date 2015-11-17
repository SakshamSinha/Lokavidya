'use strict';

angular.module('lokavidyaApp')
    .controller('AudioController', function ($scope, Audio, AudioSearch) {
        $scope.audios = [];
        $scope.loadAll = function() {
            Audio.query(function(result) {
               $scope.audios = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Audio.get({id: id}, function(result) {
                $scope.audio = result;
                $('#deleteAudioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Audio.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAudioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            AudioSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.audios = result;
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
            $scope.audio = {
                name: null,
                sync: false,
                id: null
            };
        };
    });
