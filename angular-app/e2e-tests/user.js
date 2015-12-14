'use strict';

describe('User registration / log in / log off', function() {

    beforeEach(function() {
        browser.get('http://localhost:8000/app/#/products');
    });

    it('should be able to register a new user', function() {
        // Given
        element(by.id("login")).click();
        element(by.id("registerName")).sendKeys("mata");
        element(by.id("registerEmail")).sendKeys("mata@gmail.com");
        element(by.id("registerPassword")).sendKeys("1234qwer");
        element(by.id("confirmPassword")).sendKeys("1234qwer");

        // When
        element(by.id("registerBtn")).click();

        // Then
        var list = element(by.css('.nav.navbar-nav.navbar-right')).$$('li');
        expect(list.first().getText()).toEqual('欢迎光临, mata!');
        expect(list.last().isDisplayed()).toBe(true);

        var customerName = element(by.id("customerName")).evaluate('customer.name');
        expect(customerName).toBe('mata');

        var customerEmail = element(by.id("customerEmail")).evaluate('customer.email');
        expect(customerEmail).toBe('mata@gmail.com');
    });

    it('should be able to log out', function() {
        // Given & When
        var list = element(by.css('.nav.navbar-nav.navbar-right')).$$('li');

        // When
        list.last().click();

        // Then
        expect(list.first().isDisplayed()).toBe(false);
        expect(list.last().isDisplayed()).toBe(false);
    });


    it('should validate email/password upon login - incorrect password', function() {
        // Given
        element(by.id("login")).click();
        element(by.id("loginEmail")).sendKeys("mata@gmail.com");
        element(by.id("loginPassword")).sendKeys("wrongPassword");
        expect(element(by.css('.alert.alert-danger.textInputLength')).isDisplayed()).toBe(false);

        // When
        element(by.id("loginBtn")).click();

        // Then
        var error = element(by.css('.alert.alert-danger.textInputLength'));
        expect(error.isDisplayed()).toBe(true);
        expect(error.getText()).toEqual('邮箱 / 密码 貌似有误');
    });

    it('should validate email/password upon login - incorrect email', function() {
        // Given
        element(by.id("login")).click();
        element(by.id("loginEmail")).sendKeys("chen@gmail.com");
        element(by.id("loginPassword")).sendKeys("1234qwer");

        // When
        element(by.id("loginBtn")).click();

        // Then
        var error = element(by.css('.alert.alert-danger.textInputLength'));
        expect(error.isDisplayed()).toBe(true);
        expect(error.getText()).toEqual('邮箱 / 密码 貌似有误');
    });


    it('should be able to log in', function() {
        // Given
        element(by.id("login")).click();
        element(by.id("loginEmail")).sendKeys("mata@gmail.com");
        element(by.id("loginPassword")).sendKeys("1234qwer");

        // When
        element(by.id("loginBtn")).click();

        // Then
        var list = element(by.css('.nav.navbar-nav.navbar-right')).$$('li');
        expect(list.first().getText()).toEqual('欢迎光临, mata!');
        expect(list.last().isDisplayed()).toBe(true);

        var customerName = element(by.id("customerName")).evaluate('customer.name');
        expect(customerName).toBe('mata');

        var customerEmail = element(by.id("customerEmail")).evaluate('customer.email');
        expect(customerEmail).toBe('mata@gmail.com');
    });
});