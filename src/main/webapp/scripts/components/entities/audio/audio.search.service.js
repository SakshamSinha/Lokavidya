'use strict';

angular.module('lokavidyaApp')
    .factory('AudioSearch', function ($resource) {
        return $resource('api/_search/audios/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
