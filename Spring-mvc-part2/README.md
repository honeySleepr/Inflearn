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
