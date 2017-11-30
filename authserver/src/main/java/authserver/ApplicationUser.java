package authserver;

public final class ApplicationUser {

    private String username;
    private String password;

    public ApplicationUser(){}

    public ApplicationUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGuest(){
        return username.equals("guest");
    }
}
