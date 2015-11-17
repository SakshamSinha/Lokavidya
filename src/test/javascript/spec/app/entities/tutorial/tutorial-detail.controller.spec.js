'use strict';

describe('Tutorial Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockTutorial, MockProject, MockUser, MockComment, MockExtVideo, MockSegmentVideo, MockOwnership;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockTutorial = jasmine.createSpy('MockTutorial');
        MockProject = jasmine.createSpy('MockProject');
        MockUser = jasmine.createSpy('MockUser');
        MockComment = jasmine.createSpy('MockComment');
        MockExtVideo = jasmine.createSpy('MockExtVideo');
        MockSegmentVideo = jasmine.createSpy('MockSegmentVideo');
        MockOwnership = jasmine.createSpy('MockOwnership');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Tutorial': MockTutorial,
            'Project': MockProject,
            'User': MockUser,
            'Comment': MockComment,
            'ExtVideo': MockExtVideo,
            'SegmentVideo': MockSegmentVideo,
            'Ownership': MockOwnership
        };
        createController = function() {
            $injector.get('$controller')("TutorialDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'lokavidyaApp:tutorialUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
