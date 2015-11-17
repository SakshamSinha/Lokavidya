'use strict';

angular.module('lokavidyaApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


