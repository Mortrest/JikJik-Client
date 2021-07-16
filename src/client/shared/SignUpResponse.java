package client.shared;

public class SignUpResponse {

    private final String username;
    private final String fName;
    private final String lName;
    private final String email;
    private final String password;

    public SignUpResponse(String username, String fName, String lName, String email, String password){
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
