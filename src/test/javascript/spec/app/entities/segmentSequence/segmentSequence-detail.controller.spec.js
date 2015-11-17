'use strict';

describe('SegmentSequence Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSegmentSequence, MockSegmentVideo;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSegmentSequence = jasmine.createSpy('MockSegmentSequence');
        MockSegmentVideo = jasmine.createSpy('MockSegmentVideo');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'SegmentSequence': MockSegmentSequence,
            'SegmentVideo': MockSegmentVideo
        };
        createController = function() {
            $injector.get('$controller')("SegmentSequenceDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'lokavidyaApp:segmentSequenceUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
