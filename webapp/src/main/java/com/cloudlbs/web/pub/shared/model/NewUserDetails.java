package com.cloudlbs.web.pub.shared.model;

import java.io.Serializable;

public class NewUserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String password;

    /**
     * Required for GWT serialization
     */
    public NewUserDetails() {
    }

    public NewUserDetails(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
