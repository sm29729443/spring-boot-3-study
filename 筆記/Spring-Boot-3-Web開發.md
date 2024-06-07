---
aliases:
  - springboot3
  - spring-boot-3

Created-Date: 2024-06-07T12:32:00
Last-Modified-Date: 2024-06-07T12:32:00
學習資源: "尚硅谷影片"
學習影片連結: "https://www.bilibili.com/video/BV1Es4y1q7Bf?p=4&spm_id_from=pageDriver&vd_source=761768e2c11632de30fd3e6fab20e591"
影片筆記連結: https://www.yuque.com/leifengyang/springboot3/vznmdeb4kgn90vrx#cYdbS
---

# Spring-Boot3-Web 開發

## Web 的自動配置流程與默認配置

### 自動配置

- 1.導入 maven dependency

    ```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    ```

- 2.spring-boot 註冊 auto configuration

    spring-boot 會透過 `@SpringBootApplication`&rarr;`@EnableAutoConfiguration`&rarr;`@Import(AutoConfigurationImportSelector.class)`去註冊`AutoConfigurationImportSelector`成為 Bean。

    接者透過`AutoConfigurationImportSelector`&rarr;`getCandidateConfigurations()`&rarr;`ImportCandidates.load()`去加載`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`裡定義的自動配置類。

    而在這些自動配置類中，會透過條件註解去判斷此自動配置類是否要生效。

    有的自動配置類會透過`@EnableConfigurationProperties`去綁定`xxxProperties.class`，而這個**屬性配置類**會透過`@ConfigurationProperties`去綁定到`application.properties`的屬性上。

    補充:條件註解是成立時才會把 class 放到 IOC Container，而不是先放到 IOC Container 再判斷自動配置類是否該生效。

- 3.所有 Web 相關的自動配置類如下(不含響應式場景):

    ```file
    org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration
    org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
    org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
    org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
    org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
    org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration
    org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration
    org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
    ```

- 4.`application.properties`中 Web 相關的設定:
  - 1.spring MVC 相關配置: prefix = `spring.mvc`。
  - 2.Web 相關配置: prefix = `spring.web`。
  - 3.文件上傳配置: prefix = `spring.servlet.multipart`。
  - 4.Web server 相關配置: prefix = `server`。

### 默認配置

1. 包含了 ContentNegotiatingViewResolver 和 BeanNameViewResolver 元件，方便視圖解析。

    這是給 Thymeleaf、jsp 這些前後端不分離的項目使用的，若是前後端分離就直接 return json 數據而已。

2. 預設的靜態資源處理機制： 靜態資源放在 static 資料夾下即可直接存取。

3. 自動註冊了 Converter,GenericConverter,Formatter元件，適配常見資料類型轉換和格式化需求。

    目前理解是，spring-boot 在把 `application.properties`的屬性綁定到 class 上時要使用的。

    因為在`application.properties`裡的值都只是 string，因此綁定到 class 上時要進行類型轉換跟格式化，譬如`day=2024/06/07`&rarr;`Date day;`就要進行類型上的轉換及決定日期格式的樣子，而這些就是 Converter、Formatter 的功能。

4. 支援 HttpMessageConverters，可以方便返回json等資料類型。

    當使用`@ResponseBody`時，spring-boot 會把 return 的物件轉換成 json 格式，若是 return string 則直接輸出，這就是 HttpMessageConverters 的功能。

5. 註冊 MessageCodesResolver，方便國際化及錯誤訊息處理。
6. 支援 靜態 index.html。
7. 自動使用ConfigurableWebBindingInitializer，實現訊​​息處理、資料綁定、型別轉換、資料校驗等功能。

#### 重要

- 如果想在 spring-boot 提供的 mvc 默認配置上，自定義更多的 mvc 配置，譬如添加 interceptor、formatters 等。那可以使用`@Configuration` + implement `WebMvcConfigurer.interface`在配置類上，但**不要添加`@EnableWebMvc`**。

- 如果想要保持 spring-boot 提供的 mvc 默認配置，但要自訂核心元件實例，例如：RequestMappingHandlerMapping, RequestMappingHandlerAdapter, 或ExceptionHandlerExceptionResolver，給容器中放一個 WebMvcRegistrations 元件即可。

- 如果想全面接管 Spring MVC，也就是不使用任何 spring-boot 提供的默認配置，那在配置類上添加`@Configuration`，並加上`@EnableWebMvc`註解，實現 WebMvcConfigurer 接口。

## 靜態資源

### 0. `WebMvcAutoConfiguration` 原理

#### 1. 生效條件

```java
@AutoConfiguration(after = { DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
		ValidationAutoConfiguration.class }) //此配置要在這些配置之後才動作
@ConditionalOnWebApplication(type = Type.SERVLET) //如果是 web application 才生效
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class) // IOC Container 中沒有這個 Bean 才生效
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)// 設定自動配置類的加載順序
@ImportRuntimeHints(WebResourcesRuntimeHints.class)
public class WebMvcAutoConfiguration { 
}
```

#### 2.`HiddenHttpMethodFilter`、`FormContentFilter` 功能

會看到 `WebMvcAutoConfiguration` 一開始註冊了兩個 Bean 為 `OrderedHiddenHttpMethodFilter`、`OrderedFormContentFilter`，它們分別繼承了`HiddenHttpMethodFilter`、`FormContentFilter`。

##### `OrderedHiddenHttpMethodFilter` 功能

結論:因為 HTML Form 只具備 GET、POST 兩種 Method，此 Filter 就是讓 HTML Form 也具備 PUT、DELETE 等其他 Method，如果是由 js 發送 http request 的話，則不會用到此 Filter，因為 js 本來就支持發送 PUT、DELETE 等 http request method。

實際上，瀏覽器的 HTML Form 表單只支持發送 GET、POST 兩種 http request，而可以發送 PUT、DELETE 實際上是由後端做了特別的處理的，在 Spring MVC 就是由`OrderedHiddenHttpMethodFilter`來做了這部分的處理。

- `OrderedHiddenHttpMethodFilter` 處理 PUT、DELETE 的過程

  如果想讓`OrderedHiddenHttpMethodFilter`可以處理 PUT、DELETE 必須先滿足以下條件

  - 1.當前的 http request method 必須是 POST。
  - 2.當前的 http request 必須攜帶 request parameter _method。
  
  根據上述 2 點，HTML Form 應該長這樣

    補充: input 標籤傳遞到後端時 name、value 會以 key-value 的方式對應者，因此後端透過 `request.getParameter("_method")`就會得到 put。

    ```html
    <form action="/update" method="post">
        <input type="hidden" name="_method" value="put">
        <input type="text" name="username" value="JohnDoe">
        <input type="submit" value="Update">
    </form>
    ```

    觀看`HiddenHttpMethodFilter.class`:

    可以看到一開始會先判斷當前 request.method 是否為 POST，然後透過 `request.getParameter(this.methodParam)` 去獲得 key 為 `_method` 的 value 值。

    ```java
    public class HiddenHttpMethodFilter extends OncePerRequestFilter {
    private static final List<String> ALLOWED_METHODS;
    public static final String DEFAULT_METHOD_PARAM = "_method";
    private String methodParam = "_method";

        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest requestToUse = request;
        if ("POST".equals(request.getMethod()) && request.getAttribute("jakarta.servlet.error.exception") == null) {
            String paramValue = request.getParameter(this.methodParam);
            if (StringUtils.hasLength(paramValue)) {
                String method = paramValue.toUpperCase(Locale.ENGLISH);
                if (ALLOWED_METHODS.contains(method)) {
                    requestToUse = new HttpMethodRequestWrapper(request, method);
                }
            }
        }

        filterChain.doFilter((ServletRequest)requestToUse, response);
    }
    }
    ```

##### `OrderedFormContentFilter` 功能
