package com.atguigu.boot304web;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;

@JacksonXmlRootElement  // 允許寫出為 xml 格式
@Data
@AllArgsConstructor
public class Person {
    private Long id;
    private String userName;
    private String email;
    private Integer age;
}
