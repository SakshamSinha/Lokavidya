'use strict';

angular.module('lokavidyaApp')
    .factory('CommentSearch', function ($resource) {
        return $resource('api/_search/comments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
