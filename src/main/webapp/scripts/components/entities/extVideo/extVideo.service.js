'use strict';

angular.module('lokavidyaApp')
    .factory('ExtVideo', function ($resource, DateUtils) {
        return $resource('api/extVideos/:id', {}, {
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
