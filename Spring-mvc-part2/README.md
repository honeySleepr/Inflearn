# 스프링 MVC 2편

> Thymeleaf 를 쓰는 부분은 거의 건너뛰었다

### BindingResult

> 검증 오류를 보관하는 객체

* 메서드 인자에서 @ModelAttribute 객체 바로 다음에 위치해야한다
* `BindingResult`가 없는 상태에서 `@ModelAttribute`에서 데이터 바인딩 오류가 나면 컨트롤러가 호출되지 않는다(= 400에러 화면이 표시된다)
* `if(bindingResult.hasErrors())`를 이용해 error가 발생한 경우 특정 로직을 실행하도록 할 수 있다 (ex: 원래 페이지로 redirect)

##### TypeMismatch (타입 변환 에러)

> int 필드에 String을 입력했을 때와 같이 타입 변환이 실패했을 때 발생한다

이때 기본 오류 메시지가 아닌 커스텀 메시지를 만들려면
`messages.properties` 에 다음 내용을 추가한다

```java
typeMismatch.java.lang.Integer=숫자를 입력해주세요.
	typeMismatch=타입 오류입니다.
```

다른 파일에 만들어주고 싶다면 application 설정 파일에 `spring.messages.basename=파일명`을 추가하고
`파일명.properties` 파일을 생성하여 위와 똑같이 해준다

### Bean Validation

build.gradle에 다음 의존성을 추가해주면
`implementation ‘org.springframework.boot:spring-boot-starter-validation’`

스프링 부트에서 자동으로 `LocalValidatoryFactoryBean`을 글로벌 Validator로 등록한다 -> Annotation 기반의 검증기

객체에 **@Validated** 또는 **@Valid**를 붙여주면, 해당 객체에 대해서 Validator가 작동하게 된다

* @Validated : Spring 프레임워크의 기능
* @Valid : Java 표준 기능. Spring 외의 다른 프레임워크에서 사용할 수 있다

    * 어느걸 쓰던 크게 상관없다. **@Validated**에는 **groups** 기능이 포함되어 있다
    * groups는 각 필드에 걸리는 조건을 실행되는 메서드에 따라 나눌 수 있는 기능인데, 실무에선 잘 쓰이지 않는다. 이 방법 대신에 상황에 따라 DTO를 나누는 방법이 사용된다

##### 검증 로직 순서

1. @ModelAttribute에 의해 객체의 필드에 Request Parameter 값들이 입력된다
2. 1에서 타입 변환에 실패하여 값 입력이 실패하는 경우 **FieldError**가 추가된다
3. 1에서 <u>타입 바인딩에 성공한 경우에만</u> Validator가 작동한다

요약 : @ModelAttribute -> 필드 타입으로 변환 시도 -> 변환 실패 시 FieldError, 성공 시 BeanValidation 검사

---
의존성은 Web -> Domain 방향으로 단방향이어야 한다 즉, Web이 Domain에 의존하도록 해야지, **Domain이 Web에 의존하면 안된다**!

* classA가 classB에 “의존한다” 라는 건 다음과 같다
    * classA가 classB를 “알고 있다”
    * classA가 classB를 “참조한다”
    * classA가 classB를 인자 또는 필드로 가지고 있다 : `ClassB classB`
    * classA에서 classB 객체를 생성한다 : `ClassB b = new ClassB(..)`

---

## 쿠키🍪

* 영속 쿠키 : 지정된 만료 날짜 까지는 브라우저가 종료되어도 남아있는 쿠키
* 세션 쿠키 : 브라우저를 끄면 사라지는 쿠키

##### @CookiValue

메서드 인자로 쿠키를 받는다

```java
@GetMapping("/")
public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId)
```

##### 쿠키의 보안 문제

* 쿠키 값은 임의로 변경할 수 있다
* 쿠키에 저장된 정보는 쉽게 노출될 수 있다

## 세션

##### @SessionAttribute

> 메서드 인자에서 session 데이터를 바로 꺼내볼 수 있다
`@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember`

##### TrackingModes

브라우저를 새로 시작하고 처음 로그인하면 URL 뒤에 `;jsessionId=..` 가 붙는다 이건 브라우저가 쿠키를 지원하지 않는 경우를 위해 쿠키 대신 URL을 통해 세션을 유지하는 방법이다. URL 전달 방식을
끄려면 application.properties 파일에 다음 내용을 추가해주면 된다
`server.servlet.session.tracking-modes=cookie`

##### 세션 타임아웃 설정

`application.properties` 파일에서
`server.servlet.session.timeout=60`

* 분 단위로 설정(최소 60s 부터)
* 지정된 시간 동안 Http 요청이 없으면 세션이 지워지고, 요청이 있으면 해당 요청 시간을 기준으로 타임아웃 시간이 다시 리셋된다

> 세션은 **메모리**에 저장되기 때문에 최소한의 정보만을 저장해야한다

## 필터, 인터셉터

> 필터는 서블릿의 기능이고 인터셉터는 스프링 기능이다
> AOP로도 필터/인터셉터의 기능을 대체할 순 있지만, 웹과 관련된 공통관심사를 처리할 때는 웹관련 기술들이 함께 제공되는 서블릿 필터나 인터셉터를 사용하는 게 낫다.

### 서블릿 필터

> 문지기 역할을 한다  
> HTTP 요청 -> WAS -> **필터** -> 서블릿 -> 컨트롤러
> 필터에 걸리게 되면 서블릿 자체가 호출되지 않고 막힌다

##### doFilter()

> Http 요청이 올때마다 먼저 호출된다

##### 필터 구현 순서

1. `javax.servlet` 패키지의 `Filter` 인터페이스를 구현하는 클래스를 생성한다
2. `doFilter()`를 오버라이딩 하여 필터 로직을 구현한다

* 이때 `chain.doFilter(reqeust,response)`를 호출해 줘야 다음 필터를 호출하거나, 필터가 없으면 서블릿을 호출하게 된다.
* 이걸 안해주면 서블릿->컨트롤러가 호출이 안되기 때문에 페이지가 다음 단계로 넘어가질 않는다

3. 프로젝트 root 경로에 `@Configuration`을 붙인 Config 클래스를 생성한다
4. FilterRegistrationBean을 사용해 필터를 등록한다

```java
    @Bean
public FilterRegistrationBean logFilter(){
	FilterRegistrationBean<Filter> filterBean=new FilterRegistrationBean<>();
	filterBean.setFilter(new LogFilter());
	filterBean.setOrder(1);
	filterBean.addUrlPatterns("/*");

	return filterBean;
	}
	}
```

	* `filterBean.setFilter(new LogFilter())` : 1에서 생성한 클래스를 필터로 등록

### 인터셉터

> 필터가 서블릿이 제공하는 기능인 반면 인터셉터는 스프링 MVC의 기능이다  
> 필터보다 정교하고 다양한 기능을 지원한다     
> HTTP 요청 -> WAS -> 필터 -> 서블릿 -> **인터셉터** -> 컨트롤러

1. `HandlerInterceptor` 인터페이스를 구현하고, 필요한 메서드들을 오버라이딩 한다

* `preHandle()` : 컨트롤러 요청 전에 호출된다
* `postHandle()` :  컨트롤러 요청 후에 호출된다. 단, **컨트롤러에서 예외 발생 시 호출되지 않는다**!
* `afterCompletion` : 뷰가 렌더링 된 이후에 호출된다. **컨트롤러 예외 여부와 상관없이 항상 호출된다**

2. `WebMvcConfigurer`를 구현하는 Config 클래스를 만들어 `addInterceptor`에 1에서 만든 클래스를 추가한다

```java
@Override
public void addInterceptors(InterceptorRegistry registry){
	registry.addInterceptor(new LogInterceptor())
	.order(1)
	.addPathPatterns("/**")
	.excludePathPatterns("/css/**","/*.ico","/error");
	}
```

> 서블릿 필터보다 인터셉터가 사용하기 편리하다!

### ArgumentResolver

> 메서드 파라미터에 커스텀 Annotation을 적용할 수 있게 해준다

기존에 `httpRequest.getSession() -> session.getAttribute() -> Member loginMember` 형태로 꺼내야했던 것을  
메서드 파라미터에서 `@Login Member loginMember` 형태로 바로 받을 수 있다

1. 원하는 Custom Annotation을 생성한다

```java

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {

}
```

	* `@Target` : 해당 어노테이션을 파라미터에 사용하도록 설정
	* `@Retention` : 리플렉션 등을 활용할 수 있도록 Runtime 까지 어노테이션 정보가 남아있도록 설정 (??)

2. `HandlerMethodArgumentResolver` 인터페이스를 구현하는 클래스를 생성하고 메서드들을 오버라이딩 한다

* `supportsParameter` :  호출되는 컨트롤러의 메서드에서 지정된 타입의 파라미터가 쓰이는지 확인한다. true가 반환되는 경우에만 `resolveArgument()` 메서드가 호출된다
    * 처음 한번만 실행 되고 이후로는 캐쉬된 값을 사용하기 때문에 호출되지 않고 바로 `resolveArgument()`가 호출된다

```java
/* 메서드의 파라미터에 @Login이 붙어있는가 확인 */
boolean hasLoginAnnotation=parameter.hasParameterAnnotation(Login.class);

	/* 메서드 파라미터의 타입이 Member 타입인가 확인 */
	boolean hasMemberType=Member.class.isAssignableFrom(parameter.getParameterType());
	return hasLoginAnnotation&&hasMemberType;
```

	* `resolveArgument` : 컨트롤러 호출 직전에 호출되어 필요한 파라미터 정보를 생성해준다. 여기서는 @Login이 붙어있는 파라미터에 넣어줄 값을 찾아서 반환해준다. 이 값은 session에 있는 Member 인스턴스이므로 `return session.getAttribute(SessionConst.LOGIN_MEMBER);`

3. `WebMvcConfigurer`를 구현하는 Config 클래스를 생성한다
4. `addArgumentResolvers`메서드를 오버라이딩 하여 2에서 생성한 클래스를 등록한다

```java
@Override
public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
	resolvers.add(new LoginMemberArgumentResolver());
	}
```

## 예외처리

> 요약 :  예외 처리는 @ExceptionHandler + @ControllerAdvice 조합으로 깔끔하게!

기본적으로 예외가 catch 되지 않고 서블릿까지 던져지면 브라우저에서는 500 에러로 표시된다

* 요청
  `WAS -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러`
* 컨트롤러에서 에러 발생
  `WAS <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(에러)`
* 오류 페이지 다시 호출
  `WAS -> 서블릿 -> 컨트롤러 `
    * 여기서 필터와 인터셉터는 에러페이지 요청 시에는 호출되지 않도록 함
        * 필터 : `dispatchType = REQUEST`에 대해서만 호출되도록 함
        * 인터셉터 : `excludePatterns()`를 통해 에러페이지 호출 URI 제외시킴

### BasicErrorController

> 예외에 대해서 적절한 html 화면을 제공할 때 사용되는 컨트롤러

스프링 부트에서 제공하는 기능으로, `WebServerCustomizer`를 직접 만들고 ErrorPage를 추가하는 작업을 안해도 된다

**Error page 선택 우선 순위**

1. `resources/templates/error/에러코드.html`
2. `resources/static/error/에러코드.html`
3. `resources/templates/error.html`

* 여기서 html파일은 `404`처럼 에러코드를 구체적으로 명시하거나 `4xx` 형식으로 만들면 된다(`4xx`는 모든 400번대 에러에 대해서 해당 error 페이지가 사용된다는 뜻)

* 1,2에 조건에 맞는 파일이 없으면 default로 3번 파일이 호출된다

* 에러 표기 시에 어떤 정보들을 포함할지 `application.properties` 파일을 통해 설정할 수 있다

```
server.error.include-exception=true
server.error.include-message=on_param
server.error.include-stacktrace=on_param
server.error.include-binding-errors=on_param
```

### ExceptionResolver

> 핸들러에서 발생하는 예외를 바로 WAS로 바로 보내지 않고 중간에 잡아서 처리해줄 수 있게 해준다 (만약 처리하지 않고 그대로 WAS까지 예외가 던져지면 **500** 에러 발생)

![D851EC90-7421-43A1-8BC2-60650912C926](https://user-images.githubusercontent.com/92678400/181256479-19c42d80-fd02-43d8-a3a8-fd358db84fef.png)

수동으로 예외들을 잡으려면 `HandlerExceptionResolver`를 구현하는 클래스를 생성하고, `resolveException()` 메서드를 오버라이딩하여 아래와 같이 구현할 수 있다

```java
if(ex instanceof IllegalArgumentException){
	response.sendError(HttpServletResponse.SC_BAD_REQUEST,ex.getMessage());
	return new ModelAndView();
```

하지만 스프링에서 자동으로 등록해주는 `ExceptionResolver`들을 쓰면 훨씬 편하다!  다음 순서대로 우선적으로 호출된다

1. ExceptionHandlerExceptionResolver : 예외 발생 시 `@ExceptionHandler`이 붙어있는 메서드를 호출한다(**메서드 인자에 있는 예외 타입과 매치**가 되면 해당 메서드가
   호출된다)

```java
@ExceptionHandler
public ResponseEntity<ErrorResult> userExHandler(UserException e){
	ErrorResult errorResult=new ErrorResult("USER-EX",e.getMessage());
	return new ResponseEntity(errorResult,HttpStatus.BAD_REQUEST);
	}
```

2. ResponseStatusExceptionResolver : `@ResponseStatus` 어노테이션을 찾아서 `response.sendError()`를 호출하여 에러코드를 바꿔준다
3. DefaultHandlerExceptionResolver : 스프링 내부 오류를 처리해준다 (ex: 파라미터 타입이 맞지 않는 경우 발생하는 `TypeMismatchException`)

> @ExceptionHandler를 사용하면 HandlerExceptionResolver 인터페이스를 구현하지 않고도 편리하게 에러를 처리할 수 있다. 하지만, 해당 로직을 예외를 처리할 컨트롤러 안에 작성해야하기 때문에 코드가 지저분해지기도하고, 각각의 컨트롤러에 대해 @ExceptionHandler를 적용한 메서드를 만들어줘야한다. 이러한 불편함을 없애기 위해 사용하는 것이 `@ControllerAdvice`!

### @ControllerAdvice

> 지정된 컨트롤러들에서 발생한 예외들을 다 잡아준다. 각각의 예외에 대한 처리는 @ExceptionHandler를 사용한다

* `@RestControllerAdvice`는 @ControllerAdvice + @ResponseBody 와 같다

```java

@RestControllerAdvice
public class ExControllerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ResponseEntity<ErrorResult> userExHandler(UserException e) {
		ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
		return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler
	public method2(XXException e) {..}
...
}
```

#### Controller 범위 지정

> 예외를 잡을 Controller의 범위를 지정할 수 있다  
> 범위를 생략하면 모든 Controller의 예외를 잡는다

* `@ControllerAdvice(assignableTypes = {ControllerA.class, ControllerB.class})`
  특정 Controller들만 지정

* `@ControllerAdvice("com.example.teapot")`
  특정 패키지 내의 Controller들을 지정
