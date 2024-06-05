package com.atguigu.boot302demo.config;

import com.alibaba.druid.FastsqlException;
import com.atguigu.boot302demo.bean.Cat;
import com.atguigu.boot302demo.bean.Dog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: MyConfig
 * Package: com.atguigu.boot302demo
 */
@Configuration
public class MyConfig {

    @Bean
    @ConditionalOnClass(name="com.alibaba.druid.FastsqlException")
    public Cat cat01() {
        return new Cat();
    }

    @Bean
    @ConditionalOnMissingClass(value = "com.alibaba.druid.FastsqlException")
    public Dog dog01() {
        return new Dog();
    }
}
