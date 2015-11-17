 'use strict';

angular.module('lokavidyaApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-lokavidyaApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-lokavidyaApp-params')});
                }
                return response;
            }
        };
    });
