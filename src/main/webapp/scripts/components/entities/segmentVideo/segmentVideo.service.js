'use strict';

angular.module('lokavidyaApp')
    .factory('SegmentVideo', function ($resource, DateUtils) {
        return $resource('api/segmentVideos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
