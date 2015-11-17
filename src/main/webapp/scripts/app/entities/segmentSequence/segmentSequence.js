'use strict';

angular.module('lokavidyaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('segmentSequence', {
                parent: 'entity',
                url: '/segmentSequences',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.segmentSequence.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/segmentSequence/segmentSequences.html',
                        controller: 'SegmentSequenceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('segmentSequence');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('segmentSequence.detail', {
                parent: 'entity',
                url: '/segmentSequence/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.segmentSequence.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/segmentSequence/segmentSequence-detail.html',
                        controller: 'SegmentSequenceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('segmentSequence');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SegmentSequence', function($stateParams, SegmentSequence) {
                        return SegmentSequence.get({id : $stateParams.id});
                    }]
                }
            })
            .state('segmentSequence.new', {
                parent: 'segmentSequence',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/segmentSequence/segmentSequence-dialog.html',
                        controller: 'SegmentSequenceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    segmentsequence: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('segmentSequence', null, { reload: true });
                    }, function() {
                        $state.go('segmentSequence');
                    })
                }]
            })
            .state('segmentSequence.edit', {
                parent: 'segmentSequence',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/segmentSequence/segmentSequence-dialog.html',
                        controller: 'SegmentSequenceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SegmentSequence', function(SegmentSequence) {
                                return SegmentSequence.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('segmentSequence', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
