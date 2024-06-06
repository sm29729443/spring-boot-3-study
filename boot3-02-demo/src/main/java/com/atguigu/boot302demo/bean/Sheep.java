package com.atguigu.boot302demo.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName: Sheep
 * Package: com.atguigu.boot302demo.bean
 */
@Data
@ConfigurationProperties(prefix = "sheep")
public class Sheep {
    Integer id;
    String name;
    Integer age;
}
