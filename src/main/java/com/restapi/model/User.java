package com.restapi.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by bmahule on 10/13/17.
 */
@Entity
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @NotNull
    @Column(name = "id", updatable = false, nullable = false)
    private String ID;

    @NotBlank
    @Column
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
