'use strict';

/* https://github.com/angular/protractor/blob/master/docs/toc.md */

describe('my app', function() {

  browser.get('/#');

  it('should display correct title', function() {
    expect(browser.getTitle()).toMatch("Product Page");
  });

});
