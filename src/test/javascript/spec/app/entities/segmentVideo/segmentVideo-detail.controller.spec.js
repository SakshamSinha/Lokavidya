'use strict';

describe('SegmentVideo Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSegmentVideo, MockSegment, MockTutorial, MockSegmentSequence;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSegmentVideo = jasmine.createSpy('MockSegmentVideo');
        MockSegment = jasmine.createSpy('MockSegment');
        MockTutorial = jasmine.createSpy('MockTutorial');
        MockSegmentSequence = jasmine.createSpy('MockSegmentSequence');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'SegmentVideo': MockSegmentVideo,
            'Segment': MockSegment,
            'Tutorial': MockTutorial,
            'SegmentSequence': MockSegmentSequence
        };
        createController = function() {
            $injector.get('$controller')("SegmentVideoDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'lokavidyaApp:segmentVideoUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
