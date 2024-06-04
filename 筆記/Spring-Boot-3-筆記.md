---
aliases:
  - springboot3
  - spring-boot-3

Created-Date: 2024-06-04T13:26:00
Last-Modified-Date: 2024-06-04T13:26:00
學習資源: "尚硅谷影片"
學習影片連結: "https://www.bilibili.com/video/BV1Es4y1q7Bf?p=4&spm_id_from=pageDriver&vd_source=761768e2c11632de30fd3e6fab20e591"
影片筆記連結: https://www.yuque.com/leifengyang/springboot3/vznmdeb4kgn90vrx#cYdbS
---

# Spring-Boot-3 學習筆記

## 1. 快速建立一個 spring-boot project

**程式碼為 module:boot3-01-demo**

### Step.1 創建 Maven Project

首先在 IDEA 建立一個 Empty Maven Project，並且在 pom.xml 導入 `spring-boot-starter-parent`。

```xml
    <!--所有 springboot project 都必須繼承於 spring-boot-starter-parent-->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
    </parent>
```

### Step.2 導入 Starter

在 Springboot 中，提供了一系列的 Starter，只要想使用什麼功能，只要導入對應的 Starter 即可。

譬如，想使用 web 相關的功能，只要導入 `spring-boot-starter-web` 即可。

只要是官方提供的 Starter，命名皆為:`spring-boot-starter-*`。

只要是第三方提供的 Starter，命名皆為:`*-spring-boot-starter`。

```xml
    <!--導入 web 開發的 starter-->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
```

### Step.3 創建 Springboot 主程序

在 java 的 entrypoint 編寫`SpringApplication.run(Main.class, args)`，並在 class 上加入 `@SpringBootApplication` Annotation。

```java
// springboot 主程序
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
```

### Step.4 創建 Controller

以上 Step.1 ~ Step.3 都完成後，即可創建一個 Controller 來接受 request。

```java
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello 123";
    }
}
```

### Step.5 啟動 Springboot 測試程式碼

因為 spring boot 默認啟動的 port 為 8080，但我的 8080 已經被 oracle 佔據了，因此有先加了一個 application.properties 指定 `server.port = 8081`。

訪問 localhost:8081/hello。確認執行成功。

![call request 圖片](img/Snipaste_2024-06-04_13-56-54.jpg)

### 打包 springboot

若是想打包 springboot 須在 maven 導入 springboot 提供的插件。

```xml
    <!--springboot 提供的打包插件-->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

接下來執行 `mvn clean package` 生成 jar 包，即可透過 `java -jar demo.jar` 運行 springboot project。

![java 運行圖片](img/Snipaste_2024-06-04_14-06-39.jpg)

#### springboot 提供的打包插件與 maven 原生的差別

springboot 可以直接打包成 jar 檔運行，而不必放到 web server，就是因為已經內嵌了 tomcat，而透過 springboot 插件打包出來的 jar 檔，也包含者 tomcat 等第三方依賴，因此才可直接透過 `java -jar`運行。

##### jar 檔差別

maven 打包的 jar 檔只會包含者這個 project 自身的`.class file`和 `resource file` ，並不包含者第三方依賴的 jar 檔，因此運行時得手動添加使用到的第三方依賴。

springboot 打包的 jar 檔會包含者 project 自身的 `.class file`和`resource file`外，也包含者第三方依賴，因此運行時只要確保環境有安裝 java，即可透過 `java -jar`運行 springboot 程式。

##### war 檔差別

war 檔的差異則較小，不管是 maven、springboot 打包出來的 war 檔都會包含者第三方依賴，而springboot 的 war 檔則可以自由的選擇要用內嵌的 web server 還是把 springboot.war 放到外部 web server 運行。

