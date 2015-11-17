'use strict';

angular.module('lokavidyaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('audio', {
                parent: 'entity',
                url: '/audios',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.audio.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/audio/audios.html',
                        controller: 'AudioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('audio');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('audio.detail', {
                parent: 'entity',
                url: '/audio/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.audio.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/audio/audio-detail.html',
                        controller: 'AudioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('audio');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Audio', function($stateParams, Audio) {
                        return Audio.get({id : $stateParams.id});
                    }]
                }
            })
            .state('audio.new', {
                parent: 'audio',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/audio/audio-dialog.html',
                        controller: 'AudioDialogController',
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
                        $state.go('audio', null, { reload: true });
                    }, function() {
                        $state.go('audio');
                    })
                }]
            })
            .state('audio.edit', {
                parent: 'audio',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/audio/audio-dialog.html',
                        controller: 'AudioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Audio', function(Audio) {
                                return Audio.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('audio', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
