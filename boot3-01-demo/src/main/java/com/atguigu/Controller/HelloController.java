package com.atguigu.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: HelloController
 * Package: com.atguigu.Controller
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello 123";
    }
}
