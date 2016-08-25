(function() {
    'use strict';
    angular
        .module('p04App')
        .factory('Foo', Foo);

    Foo.$inject = ['$resource', 'DateUtils'];

    function Foo ($resource, DateUtils) {
        var resourceUrl =  'api/foos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.zonedDateTime = DateUtils.convertDateTimeFromServer(data.zonedDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
