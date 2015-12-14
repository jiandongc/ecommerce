'use strict';

describe('shopping carts', function() {

    beforeEach(function() {
        browser.get('http://localhost:8000/app/#/products');
    });

    it('should display all products', function() {
        var items = element.all(by.repeater('product in products'));
        expect(items.count()).toBe(20);
    });

});