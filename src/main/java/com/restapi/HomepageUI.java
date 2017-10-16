package com.restapi;

import com.restapi.model.Tweet;
import com.restapi.repository.TweetsRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;

/**
 * Created by bmahule on 10/16/17.
 */
@SpringUI
@Theme("valo")
class HomepageUI extends UI {
    @WebServlet(value = "/*", asyncSupported = true)
    public static class Servlet extends VaadinServlet {
    }

    TweetsRepository tweetsList;
    Grid<Tweet> grid;

    @Autowired
    public HomepageUI(TweetsRepository tweetsList) {
        this.tweetsList = tweetsList;
        this.grid = new Grid<>(Tweet.class);
    }

    @Override
    protected void init(VaadinRequest request) {
        if (VaadinSession.getCurrent().getCsrfToken() != null) {
            setContent(new Button("Click me", e -> Notification.show("Hello Spring+Vaadin user!")));
            VerticalLayout mainLayout = new VerticalLayout(grid);
            setContent(mainLayout);
            grid.setHeight(300, Unit.PIXELS);
            grid.setCaption("My Tweets");
            grid.setSizeFull();
            mainLayout.addComponent(grid);
            mainLayout.setExpandRatio(grid, 1);
            setContent(new Label("Welcome to twitter rest api demo<br/><a href='/api/feed'>Your Tweets</a>", ContentMode.HTML));
        } else {
            System.out.println(VaadinSession.getCurrent().getSession().getAttributeNames().toString());
        }
    }
}
