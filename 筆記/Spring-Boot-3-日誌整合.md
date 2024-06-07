---
aliases:
  - springboot3
  - spring-boot-3

Created-Date: 2024-06-06T15:43:00
Last-Modified-Date: 2024-06-06T15:43:00
學習資源: "尚硅谷影片"
學習影片連結: "https://www.bilibili.com/video/BV1Es4y1q7Bf?p=4&spm_id_from=pageDriver&vd_source=761768e2c11632de30fd3e6fab20e591"
影片筆記連結: https://www.yuque.com/leifengyang/springboot3/vznmdeb4kgn90vrx#cYdbS
---

# Spring-Boot-3 日誌整合

對應程式碼 module:boot3-03-logging

## 1.簡介

這段直接複製來的，沒很明白差異在哪。

 1. Spring使用commons-logging作為內部日誌，但底層日誌實作是開放的。可對接其他日誌框架。

    a. spring5以後 commons-logging被spring直接自己寫了。

 2. 支持 jul，log4j2,logback。 SpringBoot 提供了預設的控制台輸出配置，也可以配置輸出為檔案。

 3. logback是預設使用的。

 4. 雖然日誌框架很多，但我們不用擔心，使用 SpringBoot 的預設配置就能運作的很好。

## spring-boot 如何自動配置日誌的

 1. `spring-boot-starter`會導入`spring-boot-starter-logging`，所以只要是使用 spring-boot 一定會有日誌功能，也不用自己去配置 maven dependency。
 2. 默認使用`logback` + `slf4j` 做為日誌的底層實現。
 3. 因為日誌是系統一啟動馬上就要使用到，要使用的時機非常早，因此若透過 xxxAutoConfiguration 的機制來配置的話太晚了，所以是透過監聽器`ApplicationListener`進行配置的。
 4. **日誌的所有配置也都能透過修改 `application.properties` 來修改，以`logging`開頭**。

## 日誌格式

```shell
2024-06-06T17:56:53.349+08:00  INFO 12636 --- [boot3-02-demo] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
```

默認輸出格式:

- 時間與日期：毫秒精度
- 日誌等級：ERROR, WARN, INFO, DEBUG, or TRACE.
- 進程 ID
- ---： 訊息分割符
- 執行緒名稱： 使用[]包含
- Logger 名： 通常是產生日誌的類別名稱
- 訊息： 日誌記錄的內容

可以在 `application.properties` 去單獨修改一些細項。

```properties
logging.pattern.dateformat=yyyy-MM-dd HH:mm:ss.SSS
```

修改後結果:

```shell
2024-06-06 18:05:39.133  INFO 23188 --- [boot3-03-logging] [           main] c.a.b.Boot303LoggingApplication          : Started Boot303LoggingApplication in 0.922 seconds (process running for 1.182)
```

## 如何紀錄日誌

- 使用 Lombok 的 @Slf4j。

- 自己引入 Logger。
  
  ```java
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  Logger logger = LoggerFactory.getLogger(getClass());
  ```

## 日誌級別

- 由低到高：ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF。
  - ***只會列印指定等級及以上等級的日誌。***
  - ALL：列印所有日誌
  - TRACE：追蹤框架詳細流程日誌，一般不使用
  - DEBUG：開發除錯細節日誌
  - INFO：關鍵、感興趣資訊日誌
  - WARN：警告但不是錯誤的訊息日誌，例如：版本過時
  - ERROR：業務錯誤日誌，例如出現各種異常
  - FATAL：致命錯誤日誌，例如jvm系統崩潰
  - OFF：關閉所有日誌記錄
- spring-boot 默認使用 root 指定的級別當作默認級別，而 root 默認級別為 INFO，因此 spring-boot 的默認級別也是 INFO。
- 可以透過`application.properties`去更改 spring-boot 的級別。

    當把級別更改為 debug 後，再重跑 spring-boot 能看到多了很多 debug 級別的詳細訊息。

  ```properties
  logging.level.root=debug
  ```

    也能單獨的去調某個 class 的級別`logging.level.<logger-name>=<level>`。

    ```properties
    logging.level.com.atguigu.boot303logging.controller.HelloController=info
    ```

## 日誌分組

**spring-boot 默認提供了 web、sql 兩個組別。**

可以將以下 aaa、bbb、ccc 透過日誌分組的功能分成一組。

```properties
logging.level.aaa=info
logging.level.bbb=info
logging.level.ccc=info
```

將 aaa、bbb、ccc 分組完後再調整日誌級別。

```properties
logging.group.abc=com.atguigu.loggin.controller.aaa,com.atguigu.loggin.controller.bbb,com.atguigu.loggin.controller.ccc
logging.level.abc=info
```

## 日誌文件輸出

spring-boot 默認只在 console 列印日誌訊息，若想把日誌訊息紀錄到一個文件，可以在`application.properties`中使用`logging.file.name`或`logging.file.path`。

## 日誌的歸檔與切割

若假設今天這個 spring-boot 運行了一年，那日誌的檔案就會非常大，所以若希望日誌是以天為單位儲存到一個文檔中，就需要使用到歸檔功能。

而也有可能出現一天的日誌訊息就非常大，因此可以透過切割功能，將一個文件大小限定在 10 MB 左右，超過的就切割成另一個文件儲存。

application.properties 配置項:

必須使用 spring-boot 默認提供的 logback 才能有以下功能，若是有自己把底層日誌系統換成 log4j 等其他的，就得自己手動加入配置檔。

- `logging.logback.rollingpolicy.file-name-pattern`:日誌存檔的檔案名稱格式（預設值：${LOG_FILE}.%d{yyyy-MM-dd}.%i.gz）
- `logging.logback.rollingpolicy.clean-history-on-start`:應用程式啟動時是否清除先前存檔（預設值：false）
- `logging.logback.rollingpolicy.max-file-size`:存檔前，每個日誌檔案的最大大小（預設值：10MB），同天內若超過就會自動切割。
- `logging.logback.rollingpolicy.total-size-cap`:日誌檔案被刪除之前，可以容納的最大大小（預設值：0B）。設定1GB則磁碟儲存超過 1GB 日誌後就會刪除舊日誌文件
- `logging.logback.rollingpolicy.max-history`:日誌檔案保存的最大天數(預設值：7).

## 自定義日誌配置

如果想要引入自己的日誌配置，只要在 resource 下添加 `logback.xml` 之類的日誌配置文件即可。

***日誌的自定義配置文件必須按照以下表格命名規範才行。***

配置文件檔名最好以`xx-spring.xml`命名，似乎是這樣的命名方式才可以讓 spring 完全控制日誌系統。

|日誌系統|自定義日誌配置文件|
|-------|-----------------|
|Logback|logback-spring.xml, logback-spring.groovy, logback.xml, or logback.groovy|
|Log4j2 |log4j2-spring.xml or log4j2.xml|
|JDK (Java Util Logging)|logging.properties|

## 排除第三方框架日誌

某些第三方框架可能自身也帶有日誌，但 spring-boot 底層已經控制好了日誌，因此導入時可以先用 `<exclusion>` 去排除第三方框架的日誌包，以免出現些衝突等等。
