package com.restapi;

import com.restapi.model.Tweets;
import com.restapi.repository.TweetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by bmahule on 10/10/17.
 */

@RestController
@EnableWebMvc
//@RequestMapping("/feed")
public class TweetsController {
    @Autowired
    TweetsRepository tweetsRepository;

    // Get All feeds
    @GetMapping("/api/feed")
    public List<Tweets> getAllTweets() {
        return tweetsRepository.findAll();
    }

    // Create a new feed item
    @PostMapping("/api/feed")
    public Tweets createNewTweet(@Valid @RequestBody Tweets tweet) {
        return tweetsRepository.save(tweet);
    }

    // Get a Single Note

    // Update a Note

    // Delete a Note
}
