var LoginPage = {
	emailTextField: element(by.id("loginEmail")),
	passwordTextField: element(by.id("loginPassword")),
	loginBtn: element(by.id("loginBtn")),
	registerLink: element(by.partialLinkText('Register')),
	loginEmailMsg: element(by.id("loginEmailMsg")),
	loginPasswordMsg: element(by.id("loginPasswordMsg")),
	loginError: element(by.id("loginError")),

	loginWith: function(email, password){
		this.emailTextField.clear();
		this.passwordTextField.clear();
		this.emailTextField.sendKeys(email);
		this.passwordTextField.sendKeys(password);
        this.loginBtn.click();
	},

	clickOnRegisterNow: function(){
		this.registerLink.click();
	}
}

module.exports = LoginPage;