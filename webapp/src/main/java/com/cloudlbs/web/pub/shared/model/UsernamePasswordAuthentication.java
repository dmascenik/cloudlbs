package com.cloudlbs.web.pub.shared.model;

import java.io.Serializable;

public class UsernamePasswordAuthentication implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    /**
     * Required for GWT serialization
     */
    public UsernamePasswordAuthentication() {
    }

    public UsernamePasswordAuthentication(String username, String password) {
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
