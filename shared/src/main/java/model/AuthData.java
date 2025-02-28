package model;

public record AuthData(String authToken, String username) {

    public AuthData setAuthToken(String authToken) {
        return new AuthData(authToken, this.username);
    }

    public AuthData setUsername(String username) {
        return new AuthData(this.authToken, username);
    }



}

// create authdata here?