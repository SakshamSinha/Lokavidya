'use strict';

angular.module('lokavidyaApp')
    .factory('SegmentSequenceSearch', function ($resource) {
        return $resource('api/_search/segmentSequences/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
