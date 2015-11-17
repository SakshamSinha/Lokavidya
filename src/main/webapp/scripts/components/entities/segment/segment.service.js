'use strict';

angular.module('lokavidyaApp')
    .factory('Segment', function ($resource, DateUtils) {
        return $resource('api/segments/:id', {}, {
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
