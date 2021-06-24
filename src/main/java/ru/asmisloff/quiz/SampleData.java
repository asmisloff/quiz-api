package ru.asmisloff.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.asmisloff.quiz.service.UserService;

import javax.annotation.PostConstruct;

@Component
public class SampleData {

    @Autowired
    UserService userService;

    @PostConstruct
    public void init() {
        if (userService.getByName("Admin-01") == null) {
            userService.create("Admin-01", "Admin-01");
        }
    }

}
