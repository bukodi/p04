(function() {
    'use strict';

    angular
        .module('p04App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('foo', {
            parent: 'entity',
            url: '/foo?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Foos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/foo/foos.html',
                    controller: 'FooController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('foo-detail', {
            parent: 'entity',
            url: '/foo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Foo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/foo/foo-detail.html',
                    controller: 'FooDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Foo', function($stateParams, Foo) {
                    return Foo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'foo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('foo-detail.edit', {
            parent: 'foo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/foo/foo-dialog.html',
                    controller: 'FooDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Foo', function(Foo) {
                            return Foo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('foo.new', {
            parent: 'foo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/foo/foo-dialog.html',
                    controller: 'FooDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                optionalString: null,
                                requiredString: null,
                                zonedDateTime: null,
                                logical: null,
                                blobField: null,
                                blobFieldContentType: null,
                                imageBlob: null,
                                imageBlobContentType: null,
                                enumField: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('foo', null, { reload: 'foo' });
                }, function() {
                    $state.go('foo');
                });
            }]
        })
        .state('foo.edit', {
            parent: 'foo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/foo/foo-dialog.html',
                    controller: 'FooDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Foo', function(Foo) {
                            return Foo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('foo', null, { reload: 'foo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('foo.delete', {
            parent: 'foo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/foo/foo-delete-dialog.html',
                    controller: 'FooDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Foo', function(Foo) {
                            return Foo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('foo', null, { reload: 'foo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
