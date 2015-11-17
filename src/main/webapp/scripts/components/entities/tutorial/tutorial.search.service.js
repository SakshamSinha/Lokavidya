'use strict';

angular.module('lokavidyaApp')
    .factory('TutorialSearch', function ($resource) {
        return $resource('api/_search/tutorials/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
