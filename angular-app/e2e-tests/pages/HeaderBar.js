var HeaderBar = {
	navBar: element(by.css('.nav.navbar-nav.navbar-right')).$$('li'),

	userName: function(){
		return this.navBar.first();
	},

	accountLink: function(){
		return this.navBar.get(1);
	},

	loginBtn: function(){
		return this.navBar.get(2);
	},

	logoutBtn: function(){
		return this.navBar.last();
	},

	clickOnLogin: function(){
		this.loginBtn().click();
	},

}

module.exports = HeaderBar;