package com.restapi;

/**
 * Created by bmahule on 10/10/17.
 */

import com.restapi.model.Tweet;
import com.restapi.repository.TweetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@RestController
public class HomeController {

    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public void loginPage (HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        //response.setHeader("Location", "/logout");
        //return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
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

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private TweetsRepository tweetsRepository;

    @GetMapping(path="/api/tweet") // Map ONLY GET Requests
    public @ResponseBody String createNewTweet (@RequestParam String userName, @RequestParam String tweetText) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Tweet t = new Tweet();
        t.setUserName(userName);
        t.setTweetText(tweetText);
        //t.setTimestamp(timestamp.getTime());
        tweetsRepository.save(t);
        return "Saved";
    }

    @GetMapping(path="/api/feed")
    public @ResponseBody Iterable<Tweet> getAllTweets() {
        // This returns a JSON or XML with the users
        return tweetsRepository.findAll();
    }
}