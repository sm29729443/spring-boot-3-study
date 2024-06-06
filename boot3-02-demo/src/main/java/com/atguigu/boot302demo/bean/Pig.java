package com.atguigu.boot302demo.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClassName: Pig
 * Package: com.atguigu.boot302demo.bean
 */
// 要記得加上 setter、getter 給底層調用
@ConfigurationProperties(prefix = "pig")
@Component
@Data
public class Pig {
    Integer id;
    String name;
    Integer age;
}
