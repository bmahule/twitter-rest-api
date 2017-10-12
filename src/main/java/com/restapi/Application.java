package com.restapi;

/**
 * Created by bmahule on 10/10/17.
 */
//import com.restapi.TweetsController;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//
//@SpringBootApplication
//@EnableJpaAuditing
//@ComponentScan(basePackageClasses = TweetsController.class)
//public class Application {
//    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }
//}

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.ComponentScan;
import com.restapi.TweetsController;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackageClasses = TweetsController.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}