'use strict';

angular.module('lokavidyaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ownership', {
                parent: 'entity',
                url: '/ownerships',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.ownership.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ownership/ownerships.html',
                        controller: 'OwnershipController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ownership');
                        $translatePartialLoader.addPart('ownershipType');
                        $translatePartialLoader.addPart('rights');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ownership.detail', {
                parent: 'entity',
                url: '/ownership/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lokavidyaApp.ownership.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ownership/ownership-detail.html',
                        controller: 'OwnershipDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ownership');
                        $translatePartialLoader.addPart('ownershipType');
                        $translatePartialLoader.addPart('rights');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Ownership', function($stateParams, Ownership) {
                        return Ownership.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ownership.new', {
                parent: 'ownership',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ownership/ownership-dialog.html',
                        controller: 'OwnershipDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    type: null,
                                    right: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ownership', null, { reload: true });
                    }, function() {
                        $state.go('ownership');
                    })
                }]
            })
            .state('ownership.edit', {
                parent: 'ownership',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ownership/ownership-dialog.html',
                        controller: 'OwnershipDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Ownership', function(Ownership) {
                                return Ownership.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ownership', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
