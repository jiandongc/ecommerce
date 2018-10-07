'use strict';

var headerBar = require('./pages/HeaderBar.js');
var registrationPage = require('./pages/RegistrationPage.js');

describe('user registration page', function() {

    beforeEach(function() {
        browser.get('http://localhost:8000/app/#!/register');
    });

	it('header is in default state', function() {
        expect(headerBar.userName().isDisplayed()).toEqual(false);
        expect(headerBar.accountLink().isDisplayed()).toEqual(false);
        expect(headerBar.loginBtn().isDisplayed()).toEqual(true);
        expect(headerBar.logoutBtn().isDisplayed()).toEqual(false);
        
    });    

    it('username is requried', function() {
        // When
        registrationPage.registerWith("", "username@email.com", "1234qwer", "1234qwer");

        // Then
        expect(registrationPage.registerNameMsg.getText()).toEqual('Username is missing. Please check and try again.');
    });

    it('email is requried', function() {
        // When
        registrationPage.registerWith("username", "", "1234qwer", "1234qwer");

        // Then
        expect(registrationPage.registerEmailMsg.getText()).toEqual('Email is missing. Please check and try again.');
    });

    it('email should be valid', function() {
        // When
        registrationPage.registerWith("username", "abc.com", "1234qwer", "1234qwer");

        // Then
        expect(registrationPage.registerEmailMsg.getText()).toEqual('Sorry, that email doesn’t look right. Please check it’s a proper email.');
    });

    it('password is requried', function() {
        // When
        registrationPage.registerWith("username", "username@email.com", "", "");

        // Then
        expect(registrationPage.registerPasswordMsg.getText()).toEqual('Password is missing. Please check and try again.');
    });

    it('password needs to be 8 characters or more', function() {
        // When
        registrationPage.registerWith("username", "username@email.com", "1a", "1a");

        // Then
        expect(registrationPage.registerPasswordMsg.getText()).toEqual('Sorry, that password is too short. It needs to be eight characters or more.');
    });

    it('password shuld be more than 50 characters', function() {
        // When
        registrationPage.registerWith("username", "username@email.com", "A12345678901234567890123456789012345678901234567890", "A12345678901234567890123456789012345678901234567890");

        // Then
        expect(registrationPage.registerPasswordMsg.getText()).toEqual('Sorry, that password is too long. It can’t be more than 50 characters.');
    });

    it('password can not be number only', function() {
        // When
        registrationPage.registerWith("username", "username@email.com", "1234567890", "1234567890");

        // Then
        expect(registrationPage.registerPasswordMsg.getText()).toEqual('Pasword must contain a letter and a number.');
    });

    it('password can not be letters only', function() {
        // When
        registrationPage.registerWith("username", "username@email.com", "abcdefgh", "abcdefgh");

        // Then
        expect(registrationPage.registerPasswordMsg.getText()).toEqual('Pasword must contain a letter and a number.');
    });

    it('password confirm is required', function() {
        // When
        registrationPage.registerWith("username", "username@email.com", "1234ABCD", "");

        // Then
        expect(registrationPage.confirmPasswordMsg.getText()).toEqual('Please confirm your password.');
    });

    it('password confirm should match', function() {
        // When
        registrationPage.registerWith("username", "username@email.com", "1234ABCD", "1234abcd");

        // Then
        expect(registrationPage.confirmPasswordMsg.getText()).toEqual('Password does not match.');
    });

    it('successful registration', function() {
        // When
        registrationPage.registerWith("username", "username@email.com", "1234ABCD", "1234ABCD");

        // Then
        expect(headerBar.userName().isDisplayed()).toEqual(true);
        expect(headerBar.accountLink().isDisplayed()).toEqual(true);
        expect(headerBar.loginBtn().isDisplayed()).toEqual(false);
        expect(headerBar.logoutBtn().isDisplayed()).toEqual(true);
        expect(headerBar.userName().getText()).toEqual('username');
    });

    it('should not register with same email twice', function() {
        // When
        registrationPage.registerWith("anotherUser", "username@email.com", "5678ABCD", "5678ABCD");

        // Then
        expect(registrationPage.error.isDisplayed()).toEqual(true);
        expect(registrationPage.error.getText()).toEqual('This the email address is already used.');
    });

    it('successful logout', function() {
        // When
        headerBar.logoutBtn().click();

        // Then
        expect(headerBar.userName().isDisplayed()).toEqual(false);
        expect(headerBar.accountLink().isDisplayed()).toEqual(false);
        expect(headerBar.loginBtn().isDisplayed()).toEqual(true);
        expect(headerBar.logoutBtn().isDisplayed()).toEqual(false);
        expect(browser.getCurrentUrl()).toContain("/home");
    });
});