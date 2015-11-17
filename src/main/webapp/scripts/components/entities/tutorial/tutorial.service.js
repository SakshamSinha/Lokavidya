'use strict';

angular.module('lokavidyaApp')
    .factory('Tutorial', function ($resource, DateUtils) {
        return $resource('api/tutorials/:id', {}, {
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
