package com.restapi.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by bmahule on 10/14/17.
 */
@Entity
@Table(name = "Connection")
@EntityListeners(AuditingEntityListener.class)
public class Connection {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "connection_id", updatable = false, nullable = false )
    private int connectionID = 1;

    @NotNull
    @Column(name = "follower_id", updatable = false, nullable = false)
    private String followerID;

    @NotNull
    @Column(name = "followee_id")
    private String followeeID;
    public Connection () {

    }

    public Connection(String follower, String followee) {
        this.followerID = follower;
        this.followeeID = followee;
    }

    public int getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(int id) {
        this.connectionID = id;
    }

    public String getfollowerID() {
        return followerID;
    }

    public void setfollowerID(String followerID) {
        this.followerID = followerID;
    }

    public String getfolloweeID() {
        return followeeID;
    }

    public void setfolloweeID(String followeeID) {
        this.followeeID = followeeID;
    }

}
