'use strict';

angular.module('lokavidyaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tutorial', {
                parent: 'entity',
                url: '/tutorials',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.tutorial.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tutorial/tutorials.html',
                        controller: 'TutorialController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tutorial');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('tutorial.detail', {
                parent: 'entity',
                url: '/tutorial/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.tutorial.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tutorial/tutorial-detail.html',
                        controller: 'TutorialDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tutorial');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Tutorial', function($stateParams, Tutorial) {
                        return Tutorial.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tutorial.new', {
                parent: 'tutorial',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tutorial/tutorial-dialog.html',
                        controller: 'TutorialDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tutorial', null, { reload: true });
                    }, function() {
                        $state.go('tutorial');
                    })
                }]
            })
            .state('tutorial.edit', {
                parent: 'tutorial',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tutorial/tutorial-dialog.html',
                        controller: 'TutorialDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Tutorial', function(Tutorial) {
                                return Tutorial.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tutorial', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
