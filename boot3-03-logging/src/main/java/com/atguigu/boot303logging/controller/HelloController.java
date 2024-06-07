package com.atguigu.boot303logging.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * ClassName: HelloController
 * Package: com.atguigu.boot303logging.controller
 */
@RestController
public class HelloController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/hello")
    public String loggerTest() {
        logger.info("call 了 /hello");
        return "hello";
    }
}
