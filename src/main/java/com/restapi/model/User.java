package com.restapi.model;

/**
 * Created by bmahule on 10/13/17.
 */
public class User {

    private static final long serialVersionUID = 1L;

    private String ID;
    private String handle;

    public String getID() {
        return ID;
    }

    public void setID(String uid) {
        this.ID = uid;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
}
