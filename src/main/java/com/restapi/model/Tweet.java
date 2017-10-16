package com.restapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

/**
 * Created by bmahule on 10/10/17.
 */

@Entity
@Table(name = "Tweets")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Tweet {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @NotNull
    @Column(name = "user_name", updatable = false, nullable = false)
    private String userName;

    @NotBlank
    @Column(name = "tweet_text")
    private String tweetText;

    @Column(name = "time_stamp")
    @CreationTimestamp
    private Timestamp createdAt;

    public Tweet() {
    }

    public Tweet(String userName, String tweetText) {
        this.userName = userName;
        this.tweetText = tweetText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        this.userName = user;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String text) {
        this.tweetText = text;
    }

    public Timestamp getTimeStamp() { return createdAt; }
}

