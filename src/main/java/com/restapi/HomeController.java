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
import com.restapi.service.TweetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
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
    @Autowired
    private TweetsService tweetsService;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public void loginPage (HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    @RequestMapping(path="/api/follow")
    public @ResponseBody String followUser (@RequestParam String followeeId) throws JpaObjectRetrievalFailureException {
        if(followeeId.isEmpty() || followeeId == null) {
           return ("followeeId cannot be empty");
        }

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
                        return "Connection saved";
                    } else {
                        System.out.println("Connection for " + followerId + " and " + followeeId + " exists in DB");
                        return "Connection for " + followerId + " and " + followeeId + " exists in DB";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Connection is not added in DB" + e.getMessage());
                    return e.getMessage();
                }

            } else {
                System.out.println("User : " + followeeId + " does not exist in DB");
                return "User : " + followeeId + " does not exist in DB";
            }
        } catch (JpaObjectRetrievalFailureException e) {
            System.out.println(e.getStackTrace());
            return e.getMessage();
        }
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public void logoutPage (HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    @RequestMapping(path="/api/tweet") // Map ONLY GET Requests
    public @ResponseBody String createNewTweet (@RequestParam String tweetText) {
        if(tweetText.isEmpty() || tweetText == null ) {
            System.out.println("Tweet content cannot be empty");
            return "Tweet content cannot be empty";
        }

        if(tweetText.length() > 255) {
            return "Tweet content cannot more than 255 characters";
        }
        String currentUser = currentUserName();

        Tweet t = new Tweet();
        t.setUserName(currentUser);
        t.setTweetText(tweetText);
        tweetsRepository.save(t);
        return "Tweet saved";
    }


    @GetMapping(path="/api/feed" )
    public @ResponseBody Page<Tweet> findPaginatedFeed(Pageable pageable) {

        Page<Tweet> tweetPage = tweetsService.listAllByPage(pageable);
        return tweetPage;
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
    }

}
