'use strict';

angular.module('lokavidyaApp')
    .factory('ExtVideoSearch', function ($resource) {
        return $resource('api/_search/extVideos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
