'use strict';

describe('shopping carts', function() {

    beforeEach(function() {
        browser.get('http://localhost:8000/app/#/products');

    });

    it('should display all products', function() {
        var items = element.all(by.repeater('product in products'));
        expect(items.count()).toBe(20);
    });

    it('should be able to add items in shopping cart as anonymous user', function() {
        browser.ignoreSynchronization = true;
        element.all(by.repeater('product in products')).get(0).$$('button').click();
        expect(element(by.css('.nav.navbar-nav.navbar-right')).$$('li').get(2).getText()).toEqual('1 items - £10');
        element.all(by.repeater('product in products')).get(1).$$('button').click();
        expect(element(by.css('.nav.navbar-nav.navbar-right')).$$('li').get(2).getText()).toEqual('2 items - £30');

    });
});