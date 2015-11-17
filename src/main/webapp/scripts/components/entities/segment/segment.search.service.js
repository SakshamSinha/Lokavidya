'use strict';

angular.module('lokavidyaApp')
    .factory('SegmentSearch', function ($resource) {
        return $resource('api/_search/segments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
