'use strict';

angular.module('lokavidyaApp')
    .factory('SegmentVideoSearch', function ($resource) {
        return $resource('api/_search/segmentVideos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
