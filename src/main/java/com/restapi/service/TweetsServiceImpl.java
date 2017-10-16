package com.restapi.service;

import com.restapi.authentication.IAuthenticationFacade;
import com.restapi.model.Connection;
import com.restapi.model.Tweet;
import com.restapi.repository.ConnectionsRepository;
import com.restapi.repository.TweetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bmahule on 10/15/17.
 */
@Service

public class TweetsServiceImpl implements TweetsService {
    TweetsRepository tweetsRepository;
    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @Autowired
    private ConnectionsRepository connectionsRepository;

    @Autowired
    private TweetsServiceImpl(TweetsRepository tweetsRepository) {
        this.tweetsRepository = tweetsRepository;
    }

    @Override
    public Page<Tweet> listAllByPage(Pageable pageable) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String currentUser = authentication.getName();
        System.out.println(currentUser);

        List<Tweet> tweets = new ArrayList<>();
        List<String> users = new ArrayList<>();
        users.add(currentUser);
        try {

            List<Connection> connectionsList = connectionsRepository.findConnectionsForUser(currentUser);
            if(!connectionsList.isEmpty()) {
                int i = 0;
                Iterator<Connection> iterator = connectionsList.iterator();
                while (iterator.hasNext()) {
                    Connection c = iterator.next();
                    users.add(c.getfolloweeID());
                }

            } else {
                System.out.println("No connections found for this user");
            }
        } catch (JpaObjectRetrievalFailureException e) {
            System.out.println(e.getStackTrace());
        }

        for (String user : users) {
            tweets.addAll(tweetsRepository.findByUsername(user));
        }

        Collections.sort(
                tweets,
                (tweet1, tweet2) -> tweet1.getId()
                        - tweet2.getId());

        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > tweets.size() ? tweets.size() : (start + pageable.getPageSize());
        return new PageImpl<Tweet>(tweets.subList(start, end), pageable, tweets.size());

    }
}
