package com.atguigu.boot304web.controller;

import com.atguigu.boot304web.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: MyController
 * Package: com.atguigu.boot304web.controller
 */
@RestController
public class MyController {

    @GetMapping("/PersonTest")
    public Person getPerson() {
        Person person = new Person(1L, "pp", "123@gmail.com", 21);
        return person;
    }
}
