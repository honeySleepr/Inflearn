# 스프링 MVC 1편

**웹서버** : 정적인 리소스만 제공 Nginx, Apache 등이 해당

**웹 애플리케이션 서버(WAS)** : 웹서버 기능 포함 + 애플리케이션 로직 수행 Tomcat, Jetty 등이 해당

> WAS로 웹서버를 대체할 수 있지만, **비싼 애플리케이션 로직**이 정적 리소스 때문에 지장을 받을 수 있게 된다.
> 또한, WAS 장애시 오류 화면 조차도 보여줄 수 없게 된다.

* 데이터(API) 만 주고받는거면 WAS만 있어도 된다.

## 서블릿

HTTP 요청이 들어올 때 TCP/IP 연결 부터 HTTP 메시지 파싱, HTTP 응답 생성 등 웹 계층과 관련된 모든 처리를 다 해준다. 덕분에 우리의 코드는 비즈니스 로직 실행에만 집중하면 된다

WAS 안에는 **서블릿 컨테이너**가 있다.

* 서블릿 객체의 생명주기(생성, 초기화, 호출, 종료)를 관리
* 서블릿 객체는 **싱글톤**으로 관리된다! (HTTP request, response와 별개)
    * HTTP request, response는 매번 새로운 인스턴스가 생성된다.

**Thread**
서블릿 객체를 호출하는 주체 각 요청마다 쓰레드(스레드?)가 생성되어 서블릿 객체를 호출한다.

문제는 Thread **생성 비용이 비싸다**는 것! 요청이 왔을 때마다 새로 생성하면 응답속도가 늦어진다.

그 대안으로 WAS는 **Thread Pool**을 활용한다. 풀에 쓰레드를 미리 만들어 채워놓고, 요청이 오면 풀에서 하나씩 꺼내어 할당하게된다. 사용이 끝난 쓰레드는 풀에 다시 반납된다.

---

#### HTTP request 상세 로그 보기

application 파일에 아래 옵션 추가
`logging.level.org.apache.coyote.http11=debug`

* 운영 서버에서는 성능 저하를 발생시킬 수 있기 때문에 개발 단계에서만 사용하자

### HttpServletRequest

request가 살아있는 동안 **임시 저장소 활용**
`request.setAttribute(name, value)`

**세션 관리**
`request.getSession(create : true)`

**쿼리 파라미터 꺼내기**
`request.getParameterNames()`
`request.getParameter(“파라미터명”)`
`request.getParameterValues(“파라미터명”)` : 중복 값이 있을 때 다 꺼낸다 예) `username=bc&username=honeySleepr`

**JSON 형식의 body 꺼내기**

ObjectMapper를 직접 사용하여 아래와 같이 꺼내서 객체에 담을 수 있다.

```java
ServletInputStream inputStream=request.getInputStream();
	String messageBody=StreamUtils.copyToString(inputStream,StandardCharsets.UTF_8);
	// messageBody = { "username" : "bc", "age" : 20}

	HelloData helloData=objectMapper.readValue(messageBody,HelloData.class);
```

내부 로직은 이렇고, 사실 이렇게 안하고 메서드 파라미터로 바로 객체(HelloData)를 넣어서 바로 값을 객체 필드로 받을 수 있다

### HttpServletResponse

다양하게 제공되는 메서드를 활용해 응답을 세팅해줄 수 있다.

**상태값 세팅**
`response.setStatus()`

**헤더 세팅**
`response.setHeader()`

**쿠키 세팅**

```java
Cookie cookie=new Cookie("MyCookie","good");
	response.addCookie(cookie);
```

**리다이렉트(redirect) 시키기**
`response.sendRedirect(“url”);`

---

### 스프링 MVC 프레임워크

**요약**

1. FrontController에서 HTTP 요청을 받는다
2. **Handler Mapping**에서 요청된 url에 맞는 **Handler**(=Controller)를 찾는다

```java
private final Map<String, Object> handlerMappingMap=new HashMap<>();

	handlerMappingMap.put("url1",new Controller1());
	handlerMappingMap.put("url2",new Controller2());
	handlerMappingMap.put("url3",new Controller3());
```

	* handlerMapping은 위와 같은 맵에서 url을 key로 주고 해당되는 컨트롤러를 꺼내는 방식과 유사하게 작동한다고 보면된다.
	* 차이점은 Map이 아니라 Bean 컨테이너에서 해당되는 Bean을 꺼내오거나, 해당되는 Annotation이 붙어있는 Bean을 꺼내오는 방식으로 작동한다는 것이다.

3. 찾은 핸들러를 실행할 수 있는 **Handler Adapter**도 찾는다
4. 핸들러 어댑터를 통해 핸들러를 실행하여 **ModelAndView**를 반환한다.
5. **ViewResolver**를 호출하여 **View**를 반환한다
6. View를 렌더링하여 응답을 보낸다.

** HandlerMapping** (우선 호출 되는 순서대로)

1. RequestMappingHandlerMapping : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
2. BeanNameUrlHandlerMapping : 스프링 빈의 이름으로 핸들러(Bean)를 찾는다.

**HandlerAdapter** (우선 호출 되는 순서대로)

1. RequestMappingHandlerAdapter : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
2. HttpRequestHandlerAdapter : HttpRequestHandler를 구현하는 객체에 사용된다
3. SimpleControllerHandlerAdapter : Controller 인터페이스를 구현하는 객체에 사용된다

### @RequestMapping

> 클래스나 메서드에 `@RequestMapping(“url”)` 형태로 사용한다. 해당 url으로 요청이 오면 해당되는 메서드가 호출된다.
> `@RequestMapping({“url1”, “url2”})`형태로 여러개의 매핑도 가능하다

위의 HanderMapping 목록에서 우선순위 1순위인  **RequestMappingHandlerMapping**은 스프링 컨테이너에 등록된 Bean 중에 `@Controller`
또는 `@Component + @RequestMapping`이 붙어있는 클래스를 찾는다

### Model

> ModelAndView 객체를 생성하지 않고 모델을 만든다

사용 전

```java
public ModelAndView save(){
	ModelAndView mav=new ModelAndView("save-result");
	mav.addObject("member",member);
	return mav;
	}
```

사용 후

```java
public String save(Model model){
	model.addAttribute("member",member);
	return"save-result";
```

### war, jar 차이

* **war**
  톰캣 같은 WAS를 별도로 설치하는 경우, 또는 jsp를 사용할 경우에 주로 사용한다

* **jar**
  내장 톰캣으 어플레케이션을 구동하는 경우 사용한다

### 로깅(logging)

> 사용할 클래스에 @Slf4j를 붙여주고 `log.debug("name is {}", name)` 형태로 사용한다

> 로깅은 최적화가 되어있기 때문에 일반 `System.out` 에 비해 수십배 이상 성능이 뛰어나다.

SLF4J는 인터페이스, 구현체는 logback, log4j 등등.

스프링 부트는 logback을 기본으로 사용한다.

**주의** : 위의 `{}`를 사용하는 형식 대신 `(”name is” + name)` 형태로 사용하면, 해당 **레벨의 로그 출력 여부와 상관없이 불필요한 String 연산이 발생**한다. 중괄호를 쓰는 문법을
사용하도록 하자.

#### 로깅 레벨 설정

> Level : Trace > Debug > Info > Warn > Error
> 일반적으로 **개발에는 Debug**, **운영에는 Info** 레벨로 설정한다.

application 설정 파일에 `logging.level.패키지경로=레벨` 를 추가해주면 된다

* ex: `logging.level.com.bc=debug`
* default는 info이다
* Trace 쪽으로 갈 수록 더 큰 포괄적인 범위이다

### @PathVariable

> url의 경로 변수를 사용한다

```java
@GetMapping("/mapping/{변수명}")
public String mappingPath(@PathVariable(“변수명”) String data){
	...}
```

* 변수명과 파라미터 이름이 같으면(i.e. data 대신 userId로 하면) `@PathVariable String 변수명`만 입력해줘도 된다.

### Mapping 옵션 : params, headers, consumes, produces

> 순서대로 각각 특정 **url 파라미터**, **요청 header**, **요청 header content-type**, **요청 header accept-type**이 일치해야 해당 메서드로 매핑된다.

ex: `@GetMapping(value = “/mapping-header”, headers = “mode=debug”)`
-> `mode=debug` header가 있어야 매핑이 된다.

### @RequestHeader

> MultiValueMap을 통해 header를 전부 받거나, 헤더의 key를 명시하여 header 하나만 가져올 수도 있다.

#### header 전부 가져오기

```java
public String headers(@RequestHeader MultiValueMap<String, String> map){...}
```

* **MultiValueMap** : 같은 key에 대해 여러 value가 저장 될 수 있다
    * `map.get(“키값”)`을 하면 **List**가 반환된다.

#### header 하나 가져오기

`@RequestHeader(“host”) String host`

* 여기서도 key 값과 파라미터 변수명이 일치하면 `(“host”)` 부분은 생략가능

### @RequestParam

> HttpServletRequest를 사용하지 않고 파라미터로 입력값을 받을 수 있다.

사용 전

```java
public ModelAndView save(HttpServletRequest request){
	String username=request.getParameter("username");
	...}
```

사용 후

```java
public ModelAndView save(
@RequestParam("username") String name,
@RequestParam int age,
@RequestParam(required = false) String gitId,
@RequestParam(defaultValue = "guest") String id
	){..}
```

* GET 요청의 **쿼리 파라미터**와 POST 요청의 **HTML form** 둘다 **request parameter**에 해당된다
* key값과 파라미터 변수명이 같다면 `(“username”)` 부분은 생략해도 된다
