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
	error: element(by.id("error")),

	registerWith: function(name, email, password, confirmPassword){
		this.namelTextField.clear();
		this.emailTextField.clear();
		this.passwordTextField.clear();
		this.confirmPasswordTextField.clear();

		this.namelTextField.sendKeys(name);
		this.emailTextField.sendKeys(email);
		this.passwordTextField.sendKeys(password);
		this.confirmPasswordTextField.sendKeys(confirmPassword);
        this.registerBtn.click();
	}
}

module.exports = RegistrationPage;