'use strict';

describe('Video Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockVideo, MockSegment;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockVideo = jasmine.createSpy('MockVideo');
        MockSegment = jasmine.createSpy('MockSegment');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Video': MockVideo,
            'Segment': MockSegment
        };
        createController = function() {
            $injector.get('$controller')("VideoDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'lokavidyaApp:videoUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
