'use strict';

angular.module('lokavidyaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('extVideo', {
                parent: 'entity',
                url: '/extVideos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.extVideo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/extVideo/extVideos.html',
                        controller: 'ExtVideoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('extVideo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('extVideo.detail', {
                parent: 'entity',
                url: '/extVideo/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.extVideo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/extVideo/extVideo-detail.html',
                        controller: 'ExtVideoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('extVideo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ExtVideo', function($stateParams, ExtVideo) {
                        return ExtVideo.get({id : $stateParams.id});
                    }]
                }
            })
            .state('extVideo.new', {
                parent: 'extVideo',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/extVideo/extVideo-dialog.html',
                        controller: 'ExtVideoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    url: null,
                                    isYoutube: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('extVideo', null, { reload: true });
                    }, function() {
                        $state.go('extVideo');
                    })
                }]
            })
            .state('extVideo.edit', {
                parent: 'extVideo',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/extVideo/extVideo-dialog.html',
                        controller: 'ExtVideoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ExtVideo', function(ExtVideo) {
                                return ExtVideo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('extVideo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
