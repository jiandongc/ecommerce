var RegistrationPage = {
	namelTextField: element(by.id("registerName")),
	emailTextField: element(by.id("registerEmail")),
	passwordTextField: element(by.id("registerPassword")),
	confirmPasswordTextField: element(by.id("confirmPassword")),
	registerBtn: element(by.id("registerBtn")),
	registerNameMsg: element(by.id("registerNameMsg")),
	registerEmailMsg: element(by.id("registerEmailMsg")),
	registerPasswordMsg: element(by.id("registerPasswordMsg")),
	confirmPasswordMsg: element(by.id("confirmPasswordMsg")),

	registerWith: function(name, email, password, confirmPassword){
		this.namelTextField.sendKeys(name);
		this.emailTextField.sendKeys(email);
		this.passwordTextField.sendKeys(password);
		this.confirmPasswordTextField.sendKeys(confirmPassword);
        this.registerBtn.click();
	}
}

module.exports = RegistrationPage;