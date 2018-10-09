'use strict';

var headerBar = require('./pages/HeaderBar.js');
var loginPage = require('./pages/LoginPage.js');

describe('user login page', function() {

    beforeEach(function() {
        browser.get('http://localhost:8000/app/#!/login');
    });


    it('email is requried', function() {
        // When
        loginPage.loginWith("", "1234qwer");

        // Then
        expect(loginPage.loginEmailMsg.getText()).toEqual('Email is missing. Please check and try again.');
        expect(loginPage.loginError.isDisplayed()).toEqual(false);
    });

    it('email is requried', function() {
        // When
        loginPage.loginWith("abc.com", "1234qwer");

        // Then
        expect(loginPage.loginEmailMsg.getText()).toEqual('Sorry, that email doesn’t look right. Please check it’s a proper email.');
        expect(loginPage.loginError.isDisplayed()).toEqual(false);
    });

    it('password is requried', function() {
        // When
        loginPage.loginWith("chen@gmail.com", "");

        // Then
        expect(loginPage.loginPasswordMsg.getText()).toEqual('Password is missing. Please check and try again.');
        expect(loginPage.loginError.isDisplayed()).toEqual(false);
    });

    it('password needs to be 8 characters or more', function() {
        // When
        loginPage.loginWith("chen@gmail.com", "1a");

        // Then
        expect(loginPage.loginPasswordMsg.getText()).toEqual('Sorry, that password is too short. It needs to be eight characters or more.');
        expect(loginPage.loginError.isDisplayed()).toEqual(false);
    });

    it('password shuld be more than 50 characters', function() {
        // When
        loginPage.loginWith("chen@gmail.com", "A12345678901234567890123456789012345678901234567890");

        // Then
        expect(loginPage.loginPasswordMsg.getText()).toEqual('Sorry, that password is too long. It can’t be more than 50 characters.');
        expect(loginPage.loginError.isDisplayed()).toEqual(false);
    });

    it('password can not be number only', function() {
        // When
        loginPage.loginWith("chen@gmail.com", "1234567890");

        // Then
        expect(loginPage.loginPasswordMsg.getText()).toEqual('Pasword must contain a letter and a number.');
        expect(loginPage.loginError.isDisplayed()).toEqual(false);
    });

    it('password can not be letters only', function() {
        // When
        loginPage.loginWith("chen@gmail.com", "abcdefgh");

        // Then
        expect(loginPage.loginPasswordMsg.getText()).toEqual('Pasword must contain a letter and a number.');
        expect(loginPage.loginError.isDisplayed()).toEqual(false);
    });

    it('incorrect email', function() {
        // When
        loginPage.loginWith("chen@db.com", "1234qwer");

        // Then
        expect(loginPage.loginError.isDisplayed()).toEqual(true);
        expect(loginPage.loginError.getText()).toEqual('Uh on...Incorrect email / password.');
    });

    it('incorrect password', function() {
        // When
        loginPage.loginWith("chen@gmail.com", "4321qwer");

        // Then
        expect(loginPage.loginError.isDisplayed()).toEqual(true);
        expect(loginPage.loginError.getText()).toEqual('Uh on...Incorrect email / password.');
    });

    it('unsuccessful login, and follow by successful login', function() {
        // When
        loginPage.loginWith("chen@gmail.com", "4321qwer");
        // Then
        expect(loginPage.loginError.isDisplayed()).toEqual(true);
        expect(loginPage.loginError.getText()).toEqual('Uh on...Incorrect email / password.');

        // When
        loginPage.loginWith("chen@gmail.com", "1234qwer");
        // Then
        expect(headerBar.userName().isDisplayed()).toEqual(true);
        expect(headerBar.accountLink().isDisplayed()).toEqual(true);
        expect(headerBar.loginBtn().isDisplayed()).toEqual(false);
        expect(headerBar.logoutBtn().isDisplayed()).toEqual(true);
        expect(headerBar.userName().getText()).toEqual('chen');
        expect(browser.getCurrentUrl()).toContain("/account");
    });

    it('successful login, logout, and login again', function() {
        // When
        loginPage.loginWith("chen@gmail.com", "1234qwer");
        // Then
        expect(headerBar.userName().isDisplayed()).toEqual(true);
        expect(headerBar.accountLink().isDisplayed()).toEqual(true);
        expect(headerBar.loginBtn().isDisplayed()).toEqual(false);
        expect(headerBar.logoutBtn().isDisplayed()).toEqual(true);
        expect(headerBar.userName().getText()).toEqual('chen');
        expect(browser.getCurrentUrl()).toContain("/account");

        // When
        headerBar.logoutBtn().click();
        // Then
        expect(headerBar.userName().isDisplayed()).toEqual(false);
        expect(headerBar.accountLink().isDisplayed()).toEqual(false);
        expect(headerBar.loginBtn().isDisplayed()).toEqual(true);
        expect(headerBar.logoutBtn().isDisplayed()).toEqual(false);
        expect(browser.getCurrentUrl()).toContain("/home");        

        // When
        headerBar.loginBtn().click();
        loginPage.loginWith("chen@gmail.com", "1234qwer");
        // Then
        expect(headerBar.userName().isDisplayed()).toEqual(true);
        expect(headerBar.accountLink().isDisplayed()).toEqual(true);
        expect(headerBar.loginBtn().isDisplayed()).toEqual(false);
        expect(headerBar.logoutBtn().isDisplayed()).toEqual(true);
        expect(headerBar.userName().getText()).toEqual('chen');
        expect(browser.getCurrentUrl()).toContain("/account");
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