'use strict';

describe('Audio Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAudio, MockSegment;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAudio = jasmine.createSpy('MockAudio');
        MockSegment = jasmine.createSpy('MockSegment');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Audio': MockAudio,
            'Segment': MockSegment
        };
        createController = function() {
            $injector.get('$controller')("AudioDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'lokavidyaApp:audioUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
