'use strict';

angular.module('lokavidyaApp')
    .factory('ImageSearch', function ($resource) {
        return $resource('api/_search/images/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
