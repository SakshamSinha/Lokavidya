'use strict';

angular.module('lokavidyaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('video', {
                parent: 'entity',
                url: '/videos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.video.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/video/videos.html',
                        controller: 'VideoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('video');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('video.detail', {
                parent: 'entity',
                url: '/video/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.video.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/video/video-detail.html',
                        controller: 'VideoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('video');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Video', function($stateParams, Video) {
                        return Video.get({id : $stateParams.id});
                    }]
                }
            })
            .state('video.new', {
                parent: 'video',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/video/video-dialog.html',
                        controller: 'VideoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    sync: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('video', null, { reload: true });
                    }, function() {
                        $state.go('video');
                    })
                }]
            })
            .state('video.edit', {
                parent: 'video',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/video/video-dialog.html',
                        controller: 'VideoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Video', function(Video) {
                                return Video.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('video', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
