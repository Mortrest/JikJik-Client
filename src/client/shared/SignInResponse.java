package client.shared;

public class SignInResponse {

    private final String username;
    private final String password;

    public SignInResponse(String username, String password){
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
