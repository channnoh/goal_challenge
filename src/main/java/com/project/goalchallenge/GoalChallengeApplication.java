package com.project.goalchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GoalChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoalChallengeApplication.class, args);
    }

}
