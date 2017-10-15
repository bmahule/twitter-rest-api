package com.restapi;

/**
 * Created by bmahule on 10/10/17.
 */

import com.restapi.authentication.IAuthenticationFacade;
import com.restapi.model.Connection;
import com.restapi.model.Tweet;
import com.restapi.model.User;
import com.restapi.repository.ConnectionsRepository;
import com.restapi.repository.TweetsRepository;
import com.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@RestController
public class HomeController {

    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @Autowired // This means to get the bean called userRepository
    private TweetsRepository tweetsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConnectionsRepository connectionsRepository;

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public void loginPage (HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        //response.setHeader("Location", "/logout");
        //return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }

    @RequestMapping(path="/api/follow")
    public @ResponseBody void followUser (@RequestParam String followeeId) {
        String followerId = currentUserName();

        try {
            List<User> users = userRepository.findUser(followeeId);
            if(!users.isEmpty()) {
                Connection conn = new Connection();
                try {
                    conn.setfollowerID(followerId);
                    conn.setfolloweeID(followeeId);
                    List<Connection> ic = connectionsRepository.findConnection(followerId, followeeId);
                    if (ic.isEmpty()) {
                        System.out.println("Creating new connection in DB for " + followerId + " and " + followeeId);
                        connectionsRepository.save(conn);
                    } else {
                        System.out.println("Connection for " + followerId + " and " + followeeId + " exists in DB");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Connection is not added in DB" + e.getMessage());
                }

            } else {
                System.out.println("User : " + followeeId + " does not exist in DB");
            }
        } catch (JpaObjectRetrievalFailureException e) {
            System.out.println(e.getStackTrace());
        }
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public void logoutPage (HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        //response.setHeader("Location", "/");
        //return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }

    @GetMapping(path="/api/tweet") // Map ONLY GET Requests
    public @ResponseBody void createNewTweet (@RequestParam String userName, @RequestParam String tweetText) {
        if(userName.isEmpty() || userName == null) {
            System.out.println("Username cannot be empty");
            return;
        }
        if(tweetText.isEmpty() || tweetText == null ) {
            System.out.println("Tweet content cannot be empty");
            return;
        }
        String currentUser = currentUserName();
        if(userName.equalsIgnoreCase(currentUser)) {
            Tweet t = new Tweet();
            t.setUserName(userName);
            t.setTweetText(tweetText);
            tweetsRepository.save(t);
        } else {
            System.out.println("Logged in user is : " + currentUser + " doesn't match with requested user name : " + userName);
        }
    }

    @RequestMapping(path="/api/feed")
    public @ResponseBody Iterable<Tweet> findAll() {
        String currentUser = currentUserName();
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
            tweets.addAll((List<Tweet>) tweetsRepository.findAll(user));
        }

        Collections.sort(
                tweets,
                (tweet1, tweet2) -> tweet1.getId()
                        - tweet2.getId());
        return tweets;
        // Need to add condition for No Tweets
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName() {
        try {
            Authentication authentication = authenticationFacade.getAuthentication();
            return authentication.getName();
        } catch (AuthenticationCredentialsNotFoundException e) {
            throw new AuthenticationCredentialsNotFoundException("Error in getting logged in user name");
        }

        //return authentication.getPrincipal().toString();
        //org.springframework.security.ldap.userdetails.LdapUserDetailsImpl@f1229e55: Dn: uid=bob,ou=people,dc=springframework,dc=org; Username: bob; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; CredentialsNonExpired: true; AccountNonLocked: true; Not granted any authorities
    }




}
