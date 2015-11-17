'use strict';

angular.module('lokavidyaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('segmentVideo', {
                parent: 'entity',
                url: '/segmentVideos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.segmentVideo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/segmentVideo/segmentVideos.html',
                        controller: 'SegmentVideoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('segmentVideo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('segmentVideo.detail', {
                parent: 'entity',
                url: '/segmentVideo/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.segmentVideo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/segmentVideo/segmentVideo-detail.html',
                        controller: 'SegmentVideoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('segmentVideo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SegmentVideo', function($stateParams, SegmentVideo) {
                        return SegmentVideo.get({id : $stateParams.id});
                    }]
                }
            })
            .state('segmentVideo.new', {
                parent: 'segmentVideo',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/segmentVideo/segmentVideo-dialog.html',
                        controller: 'SegmentVideoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    sync: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('segmentVideo', null, { reload: true });
                    }, function() {
                        $state.go('segmentVideo');
                    })
                }]
            })
            .state('segmentVideo.edit', {
                parent: 'segmentVideo',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/segmentVideo/segmentVideo-dialog.html',
                        controller: 'SegmentVideoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SegmentVideo', function(SegmentVideo) {
                                return SegmentVideo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('segmentVideo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
