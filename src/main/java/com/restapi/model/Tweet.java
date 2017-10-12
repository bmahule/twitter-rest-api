package com.restapi.model;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by bmahule on 10/10/17.
 */

@Entity
@Table(name = "tweets")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @NotBlank
    @Column
    private String userName;

    @NotBlank
    @Column
    private String tweetText;

    @Column
//    @Temporal(TemporalType.TIMESTAMP)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @CreationTimestamp
    private Timestamp createdAt;

    public Tweet() {
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
//
//    public long getTimeStamp() { return timestamp; }
//
//    public void setTimestamp(long time) { this.timestamp = time; }
}

