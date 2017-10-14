package com.restapi.ui;
import com.restapi.model.Tweet;
import com.restapi.model.User;
import com.restapi.repository.TweetsRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import javax.servlet.annotation.WebServlet;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * Created by bmahule on 10/13/17.
 */
@SpringUI
@Theme("valo")
public class Homepage extends UI{
    private static final Logger log = LoggerFactory.getLogger(Homepage.class);
//    private LdapTemplate ldapTemplate;
//
//    private void setupLdapContext() {
//        LdapContextSource ldc = new LdapContextSource();
//        ldc.setUrl("ldap://localhost:8389");
//        ldc.setBase("dc=springframework,dc=org");
//        ldc.setAnonymousReadOnly(true);
//        ldc.afterPropertiesSet();
//        ldapTemplate = new LdapTemplate(ldc);
//        System.out.println("ldap: " + ldc.toString());
//    }
//
//    private class UserContextMapper implements ContextMapper<User> {
//        public User mapFromContext(Object ctx) {
//            DirContextAdapter context = (DirContextAdapter) ctx;
//            User u = new User();
//            u.setFirstName(context.getStringAttribute("cn"));
//            u.setLastName(context.getStringAttribute("sn"));
//            u.setUid(context.getStringAttribute("uid"));
//            System.out.println("First name: " + u.getFirstName());
//            return u;
//        }
//    }
//
//    public List<User> getAllUsers() {
//        return ldapTemplate.search(query().where("objectClass").is("user"),
//                new UserContextMapper());
//    }

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = Homepage.class, widgetset = "nl.js.spring_ldap_vaadin_table_sample.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    TweetsRepository tweetsList;
    Grid<Tweet> grid;

    @Autowired
    public Homepage(TweetsRepository tweetsList) {
        this.tweetsList = tweetsList;
        this.grid = new Grid<>(Tweet.class);
    }

    @Override
    protected void init(VaadinRequest request) {
        System.out.println("In home page");
        //setupLdapContext();
        if(VaadinSession.getCurrent().getCsrfToken() != null) {
            setContent(new Button("Click me", e -> Notification.show("Hello Spring+Vaadin user!")));
        } else {
            System.out.println(VaadinSession.getCurrent().getSession().getAttributeNames().toString());
        }
    }


//        setupLdapContext();
//
//        //setContent(grid);
//        listTweets();
//        VerticalLayout mainLayout = new VerticalLayout(grid);
//        setContent(mainLayout);
//        //setContent( VaadinSession.getCurrent().getAttribute("userName") == null ? getNewLoginLayout() : mainLayout );
//
//        grid.setHeight(300, Unit.PIXELS);
//        grid.setColumns("id", "userName", "tweetText");
//        listTweets();
//    }
//
//    private void listTweets() {
//        grid.setItems((Collection<Tweet>) tweetsList.findAll());
//    }

//    public Component getNewLoginLayout() {
//        TextField username = new TextField();
//        TextField password = new TextField();
//        Button login = new Button();
//        return new VerticalLayout(username, password, login);
//    }
}