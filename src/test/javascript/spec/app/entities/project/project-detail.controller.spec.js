'use strict';

describe('Project Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockProject, MockUser, MockTag, MockComment, MockTutorial, MockOwnership;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockProject = jasmine.createSpy('MockProject');
        MockUser = jasmine.createSpy('MockUser');
        MockTag = jasmine.createSpy('MockTag');
        MockComment = jasmine.createSpy('MockComment');
        MockTutorial = jasmine.createSpy('MockTutorial');
        MockOwnership = jasmine.createSpy('MockOwnership');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Project': MockProject,
            'User': MockUser,
            'Tag': MockTag,
            'Comment': MockComment,
            'Tutorial': MockTutorial,
            'Ownership': MockOwnership
        };
        createController = function() {
            $injector.get('$controller')("ProjectDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'lokavidyaApp:projectUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
