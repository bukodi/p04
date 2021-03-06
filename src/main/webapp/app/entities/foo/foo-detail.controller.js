(function() {
    'use strict';

    angular
        .module('p04App')
        .controller('FooDetailController', FooDetailController);

    FooDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Foo'];

    function FooDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Foo) {
        var vm = this;

        vm.foo = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('p04App:fooUpdate', function(event, result) {
            vm.foo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
