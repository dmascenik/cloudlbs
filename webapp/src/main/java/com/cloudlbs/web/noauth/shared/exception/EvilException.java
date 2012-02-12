package com.cloudlbs.web.noauth.shared.exception;

import java.io.Serializable;

public class EvilException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public EvilException() {
        super("This user is evil");
    }
    
}
