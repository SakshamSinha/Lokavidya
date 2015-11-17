'use strict';

describe('Ownership Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockOwnership, MockProject, MockTutorial, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockOwnership = jasmine.createSpy('MockOwnership');
        MockProject = jasmine.createSpy('MockProject');
        MockTutorial = jasmine.createSpy('MockTutorial');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Ownership': MockOwnership,
            'Project': MockProject,
            'Tutorial': MockTutorial,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("OwnershipDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'lokavidyaApp:ownershipUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
