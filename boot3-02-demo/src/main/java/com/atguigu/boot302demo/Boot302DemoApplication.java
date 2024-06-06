package com.atguigu.boot302demo;

import com.atguigu.boot302demo.bean.Pig;
import com.atguigu.boot302demo.bean.Sheep;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(value = Sheep.class)
public class Boot302DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Boot302DemoApplication.class, args);
        System.out.println("stop");
    }
}
