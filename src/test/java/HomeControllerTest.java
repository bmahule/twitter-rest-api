import com.restapi.model.Connection;
import com.restapi.model.User;
import com.restapi.repository.ConnectionsRepository;
import com.restapi.repository.TweetsRepository;
import com.restapi.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by bmahule on 10/15/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DataJpaTest

public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConnectionsRepository connectionsRepository;

    @Autowired
    private TweetsRepository tweetsRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void noParamGreetingShouldReturnDefaultMessage() throws Exception {

        this.mockMvc.perform(get("/username")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }


    // Testing /api/follow?followee_id=<name>
    @Test
    public void followShouldSucceedIfFolloweeExists() throws Exception {

        User follower = new User("Sam", "Sam");
        entityManager.persist(follower);

        User followee = new User("Tom", "Tom");
        entityManager.persist(followee);

        this.mockMvc.perform(get("/api/follow?followee=Tom")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void followShouldFailIfFolloweeDoesNotExist() throws Exception {
        userRepository.deleteAll();
        connectionsRepository.deleteAll();
        User follower = new User("Sam", "Sam");
        entityManager.persist(follower);

        this.mockMvc.perform(get("/api/follow?followee=Tom"));//.andDo(print()).andExpect(("$.content").equalsIgnoreCase("User : Tom does not exist in DB"));
        assertThat(connectionsRepository.findAll().iterator().hasNext()  == false);
    }


    // Testing /api/tweet?userName=<name>&tweetText<text>
    @Test
    public void tweetApiShouldSucceedForValidUserAndText() throws Exception {
        userRepository.deleteAll();
        User user = new User("Tom", "Tom");
        entityManager.persist(user);

        tweetsRepository.deleteAll();

        this.mockMvc.perform(get("/api/tweet?userName=Tom&tweetText=Hi")).andDo(print()).andExpect(status().isOk());
    }

    // Testing /api/feed
    @Test
    public void feedApiShouldSucceedForValidUser() throws Exception {
        userRepository.deleteAll();
        User user1 = new User("Bob", "Bob");
        entityManager.persist(user1);

        User user2 = new User("Sam", "Sam");
        entityManager.persist(user2);

        connectionsRepository.deleteAll();
        Connection connection = new Connection(user1.getID(), user2.getID());

        tweetsRepository.deleteAll();
        this.mockMvc.perform(get("/api/tweet?userName=Bob&tweetText=Hi from Bob"));
        this.mockMvc.perform(get("/api/tweet?userName=Sam&tweetText=Hi from Sam"));

        this.mockMvc.perform(get("/api/feed")).andDo(print()).andExpect(status().isOk()).andExpect(
                jsonPath("$.userName").value("Bob")).andExpect(
                jsonPath("$.tweetText").value("Hi from Bob")).andExpect(
                jsonPath("$.userName").value("Sam")).andExpect(
                jsonPath("$.tweetText").value("Hi from Sam"));
    }



}
