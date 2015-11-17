'use strict';

angular.module('lokavidyaApp')
    .factory('OwnershipSearch', function ($resource) {
        return $resource('api/_search/ownerships/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
