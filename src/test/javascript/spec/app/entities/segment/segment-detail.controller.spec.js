'use strict';

describe('Segment Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSegment, MockSegmentVideo, MockVideo, MockImage, MockAudio;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSegment = jasmine.createSpy('MockSegment');
        MockSegmentVideo = jasmine.createSpy('MockSegmentVideo');
        MockVideo = jasmine.createSpy('MockVideo');
        MockImage = jasmine.createSpy('MockImage');
        MockAudio = jasmine.createSpy('MockAudio');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Segment': MockSegment,
            'SegmentVideo': MockSegmentVideo,
            'Video': MockVideo,
            'Image': MockImage,
            'Audio': MockAudio
        };
        createController = function() {
            $injector.get('$controller')("SegmentDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'lokavidyaApp:segmentUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
