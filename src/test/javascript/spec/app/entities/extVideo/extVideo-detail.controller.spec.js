'use strict';

describe('ExtVideo Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockExtVideo, MockTutorial;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockExtVideo = jasmine.createSpy('MockExtVideo');
        MockTutorial = jasmine.createSpy('MockTutorial');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ExtVideo': MockExtVideo,
            'Tutorial': MockTutorial
        };
        createController = function() {
            $injector.get('$controller')("ExtVideoDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'lokavidyaApp:extVideoUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
