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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/twitter-api/")
@Api(value="twitter-like-app", description="Rest APIs used for Twitter-like application")
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

    @ApiOperation(value = "Follow an existing Twitter user", produces = "application/json")
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public void loginPage (HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    @ApiOperation(value = "Follow an existing Twitter user", produces = "application/json")
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public void logoutPage (HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    @ApiOperation(value = "Return current user name", produces = "application/json")
    @RequestMapping(value = "/username", method = RequestMethod.GET, produces = "application/json")
    public String currentUserName() {
        try {
            Authentication authentication = authenticationFacade.getAuthentication();
            return authentication.getName();
        } catch (AuthenticationCredentialsNotFoundException e) {
            throw new AuthenticationCredentialsNotFoundException("Error in getting logged in user name");
        }
    }

    @ApiOperation(value = "Follow an existing Twitter user")
    @RequestMapping(value="/follow", produces = "application/json")
    public @ResponseBody
    ResponseEntity followUser (@RequestParam String followeeId) throws JpaObjectRetrievalFailureException {
        if(followeeId.isEmpty() || followeeId == null) {
            return new ResponseEntity("followeeId cannot be empty", HttpStatus.OK);
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
                        return new ResponseEntity("Connection saved", HttpStatus.OK);
                    } else {
                        System.out.println("Connection for " + followerId + " and " + followeeId + " exists in DB");
                        return new ResponseEntity("Connection for " + followerId + " and " + followeeId + " exists in DB", HttpStatus.OK);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Connection is not added in DB" + e.getMessage());
                    return new ResponseEntity(e.getMessage(), HttpStatus.OK);

                }

            } else {
                System.out.println("User : " + followeeId + " does not exist in DB");
                return new ResponseEntity("User : " + followeeId + " does not exist in DB", HttpStatus.OK);
            }
        } catch (JpaObjectRetrievalFailureException e) {
            System.out.println(e.getStackTrace());
            return new ResponseEntity(e.getMessage(), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Create new tweet")
    @RequestMapping(value="/tweet", produces = "application/json")
    public @ResponseBody ResponseEntity createNewTweet (@RequestParam String tweetText) {
        if(tweetText.isEmpty() || tweetText == null ) {
            System.out.println("Tweet content cannot be empty");
            return new ResponseEntity("Tweet content cannot be empty", HttpStatus.OK);
        }

        if(tweetText.length() > 255) {
            return new ResponseEntity("Tweet content cannot more than 255 characters", HttpStatus.OK);
        }
        String currentUser = currentUserName();

        Tweet t = new Tweet();
        t.setUserName(currentUser);
        t.setTweetText(tweetText);
        tweetsRepository.save(t);
        return new ResponseEntity("Tweet saved", HttpStatus.OK);
    }


    //@GetMapping(path="/feed" )
    @ApiOperation(value = "Display all possible tweets")
    @RequestMapping(value="/feed", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Page<Tweet> findPaginatedFeed(Pageable pageable) {
        Page<Tweet> tweetPage = tweetsService.listAllByPage(pageable);
        return tweetPage;
    }

}
