package com.restapi;
import com.restapi.model.Connection;
import com.restapi.model.User;
import com.restapi.repository.ConnectionsRepository;
import com.restapi.repository.TweetsRepository;
import com.restapi.repository.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by bmahule on 10/15/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.application.properties")
@ActiveProfiles("test")

public class HomeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TweetsRepository tweetsRepository;
    @Autowired
    private ConnectionsRepository connectionsRepository;


    @Test
    public void loginWithValidUserThenAuthenticated() throws Exception {
        FormLoginRequestBuilder login = formLogin()
                .user("bob")
                .password("bob");

        mockMvc.perform(login)
                .andExpect(authenticated().withUsername("bob"));
    }

    @Test
    public void loginWithInvalidUserThenUnauthenticated() throws Exception {
        FormLoginRequestBuilder login = formLogin()
                .user("invalid")
                .password("invalidpassword");

        mockMvc.perform(login)
                .andExpect(unauthenticated());
    }

    @Test
    public void getUsernameShouldReturnCorrectUsername() throws Exception {
        FormLoginRequestBuilder login = formLogin()
                .user("bob")
                .password("bob");

        mockMvc.perform(login)
                .andExpect(authenticated().withUsername("bob"));

        this.mockMvc.perform(get("/username").with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("bob"));

    }

    // Testing /api/follow?followee_id=<name>
    @Test
    public void followShouldSucceedIfFolloweeExists() throws Exception {
        userRepository.deleteAll();
        User user = new User("Sam", "Sam");
        userRepository.save(user);

        connectionsRepository.deleteAll();

        this.mockMvc.perform(post("/api/follow").param("followeeId", "Sam").with(user("bob").password("bob")))
        .andDo(print()).andExpect(status().isOk())
        .andDo(print()).andExpect(content().string("Connection saved"));
    }

    // Testing /api/follow?followee_id=<name>
    @Test
    public void followShouldNotFailIfConnectionAlreadyExists() throws Exception {
        userRepository.deleteAll();
        User user = new User("Sam", "Sam");
        userRepository.save(user);

        connectionsRepository.deleteAll();

        this.mockMvc.perform(post("/api/follow").param("followeeId", "Sam").with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andDo(print()).andExpect(content().string("Connection saved"));
        this.mockMvc.perform(post("/api/follow").param("followeeId", "Sam").with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andDo(print()).andExpect(content().string("Connection for bob and Sam exists in DB"));;
    }

    @Test
    public void followShouldFailIffFolloweeDoesNotExist() throws Exception {
        userRepository.deleteAll();

        this.mockMvc.perform(post("/api/follow").param("followeeId", "Tom").with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andDo(print()).andExpect(content().string("User : Tom does not exist in DB"));
    }

    // Testing /api/tweet?tweetText<text>
    @Test
    public void tweetApiShouldSucceedForValidUserAndText() throws Exception {

        this.mockMvc.perform(post("/api/tweet").param("tweetText", "Tom").with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andDo(print()).andExpect(content().string("Tweet saved"));
    }

    @Test
    public void tweetApiShouldFailForValidUserAndEmptyText() throws Exception {

        this.mockMvc.perform(post("/api/tweet").param("tweetText", "").with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andDo(print()).andExpect(content().string("Tweet content cannot be empty"));
    }

    @Test
    public void tweetApiShouldFailForValidUserAndVeryLongText() throws Exception {
        StringBuffer outputBuffer = new StringBuffer(256);
        for (int i = 0; i < 256; i++){
            outputBuffer.append(" ");
        }
        //String tweetContent = outputBuffer.toString();

        this.mockMvc.perform(post("/api/tweet").param("tweetText", outputBuffer.toString()).with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andDo(print()).andExpect(content().string("Tweet content cannot more than 255 characters"));
    }

    // Testing /api/feed
    @Test
    public void feedApiShouldShowAllPossibleTweets() throws Exception {
        userRepository.deleteAll();
        User user = new User("Sam", "Sam");
        userRepository.save(user);

        connectionsRepository.deleteAll();
        Connection connection = new Connection("bob", "sam");
        connectionsRepository.save(connection);

        tweetsRepository.deleteAll();

        this.mockMvc.perform(post("/api/tweet").param("tweetText", "Bob").with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andDo(print()).andExpect(content().string("Tweet saved"));

        this.mockMvc.perform(post("/api/tweet").param("tweetText", "Sam").with(user("Sam").password("Sam")))
                .andDo(print()).andExpect(status().isOk())
                .andDo(print()).andExpect(content().string("Tweet saved"));


        this.mockMvc.perform(get("/api/feed").with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userName").value("bob"))
                .andExpect(jsonPath("$.content[0].tweetText").value("Bob"))
                .andExpect(jsonPath("$.content[1].userName").value("Sam"))
                .andExpect(jsonPath("$.content[1].tweetText").value("Sam"))
                .andExpect(jsonPath("$.totalElements").value(2));
                //.andDo(print()).andExpect(content().string("Tweet saved"));
    }

    // Testing /api/feed
    @Test
    public void feedApiWithPagination() throws Exception {
        userRepository.deleteAll();
        User user = new User("Sam", "Sam");
        userRepository.save(user);

        connectionsRepository.deleteAll();
        Connection connection = new Connection("bob", "sam");
        connectionsRepository.save(connection);

        tweetsRepository.deleteAll();

        for(int i = 0; i < 5; i++) {
            this.mockMvc.perform(post("/api/tweet").param("tweetText", "Bob-Tweet#" + i).with(user("bob").password("bob")))
                    .andDo(print()).andExpect(status().isOk())
                    .andDo(print()).andExpect(content().string("Tweet saved"));

            this.mockMvc.perform(post("/api/tweet").param("tweetText", "Sam-Tweet#" + i).with(user("Sam").password("Sam")))
                    .andDo(print()).andExpect(status().isOk())
                    .andDo(print()).andExpect(content().string("Tweet saved"));
        }

        this.mockMvc.perform(get("/api/feed").param("page", "0").param("size", "5").with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.numberOfElements").value(5))
                .andExpect(jsonPath("$.first").value("true"));

        this.mockMvc.perform(get("/api/feed").param("page", "1").param("size", "5").with(user("bob").password("bob")))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.numberOfElements").value(5))
                .andExpect(jsonPath("$.first").value("false"))
                .andExpect(jsonPath("$.last").value("true"));
    }

}
