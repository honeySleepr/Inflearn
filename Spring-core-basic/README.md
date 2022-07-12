# 스프링 핵심원리(기본편) - 김영한님 강의


### 스프링 부트란?

스프링 프레임워크와 다음 기능들을 함께 제공한다
* Tomcat 웹서버를 내장하고 있다.
* 빌드에 필요한 라이브러리들을 알아서 구성해준다.
* 현재 스프링 프레임워크 버전과 호환되는 최신 버전의 외부 라이브러리를 자동으로 구성해준다
* 각종 설정들이 기본값으로 설정되어있다

### 다형성
* 인터페이스를 구현한 객체 인스턴스를 실행 시점에 유연하게 변경할 수 있다.
* 클라이언트를 변경하지 않고, 서버의 구현 기능을 유연하게 변경할 수 있다.
* 코드를 역할과 구현으로 분리함으로써 다형성을 구현할 수 있다.

## SOLID
### SRP
단일 책임 원칙
> 한 클래스는 하나의 책임만 가져야 한다.

* 클라이언트 객체는 실행만 담당하고 구현 객체를 생성하고 연결하는 책임은 AppConfig가 담당

> **조용호**님 강의에서:
> 여러 사람이 한 클래스에 대해 커밋을 하고 있으면 SRT 위반!
> ->  한 클래스에 대해서는 한 사람만 커밋 하도록 클래스들을 나누자(개꿀팁)
> 클래스가 변경될 이유가 하나밖에 없다 = 응집도가 높다 = SRT 원칙을 따른다

### OCP (특히 중요)
개방 폐쇄 원칙
> 기능 확장에는 열려있되, 기존 코드의 변경에는 닫혀 있어야 한다.

* ::기존 코드를 변경하지 않고, 새로운 코드만 추가해서 기능 확장을 할 수 있어야 한다!::
* AppConfig를 통해 **사용 영역**과 **구성 영역**으로 나누고, 클라이언트 코드가 추상화에 의존함으로써
* 다른 구체 클래스로 확장을 하더라도 클라이언트 코드의 변경은 하지 않는 상태를 만듬 ——>**OCP 준수!**

### LSP
리스코프 치환 원칙
> 하위 클래스는 인터페이스의 규약을 다 지켜야한다.

* 인터페이스를 구현한 구현체를 믿고 사용하기 위해 필요한 원칙.
* ex) accelerate 메서드는 앞으로 전진하는 기능으로 정해졌는데 뒤로 가게 구현하면 LSP 위반

### ISP
인터페이스 분리 원칙
> 인터페이스도 단일 책임을 가지도록 분리해야한다.

* SRP의 인터페이스 버전이라고 할 수 있다.

### DIP (특히 중요)
의존관계 역전 원칙
> 구체화에 의존하면 안되고 추상화에 의존해야한다.

* 클라이언트는 인터페이스에만 의존해야한다. 구현 객체에는 의존하면 안된다.
* AppConfig를 쓰기 전까지는 Service 클래스에서
  `Repository repository = new MemoryRepository()`
  형태로 선언되어 추상화(Repsitory)와 구체화(MemoryRepository)에 동시에 의존하는 형태였다. -> DIP 위반!

* AppConfig을 사용함으로써 Service는 추상화인 Repository만 알고 있게 되었다. -> DIP 준수!

---
## 관심사의 분리

AppConfig가 생성자를 통해 의존관계를 주입해줌으로써
**MemberServiceImpl** 은 **구체클래스에서는 일절 관여할 필요 없이**(생성자를 통해 어떤 구현체가 주입될지 알 수 없다) Repository 인터페이스만 알고있는 상태로 실행에만 집중할 수 있다.

AppConfig가 공연 기획자 역할을 한다. 구현체들은 배우들이다. 배우들은 다른 배우들은 신경 쓸 것 없이 자신의 배역(인터페이스)만 알고 실행(대본)에만 집중하면 된다.

애플리케이션의 사용 영역(Client)과 구성(Config) 영역이 나뉘게 되었다.

---
## IoC
제어의 역전

* 기존에는 클라이언트 구현 객체가 스스로 구현 객체를 생성하고, 연결하고, 실행 하였다
* AppConfig가 등장함으로서 프로그램 제어권이 AppConfig에게 넘어갔다.

## DI
의존관계 주입

> 의존관계 주입을 사용하면 정적인 클래스 의존관계를 변경하지 않고,
> 동적인 객체 인스턴스 의존관계를 변경할 수 있다.

* 정적 클래스 의존 관계 : 컴파일 타임에 확인할 수 있는 의존 관계
* 동적 클래스 의존 관계: 런타임에 확인 할 수 있는 의존 관계
* DI 컨테이너 : 자바 객체들을 Bean 형태로 넣어두는 곳.

여기서 쓰이는 AppConfig 처럼 객체를 생성하고 의존관계를 관리해주는 역할을 하는 것을 **DI 컨테이너**(or IoC컨테이너)라고 한다!
(또는 어셈블러, 오브젝트 팩토리)

### 프레임워크 VS 라이브러리
* 내가 작성한 코드를 제어하고, 대신 실행하면 그게 바로 **프레임워크**!
* 반면에 내가 작성한 코드가 직접 제어의 흐름을 담당하면 그게 **라이브러리**!

---
## @Configuration

> 설정 정보로 사용할 클래스를 지정하는 Annotation

### @Bean

> Bean으로 사용할 메서드를 지정하는 Annotation

* @Bean이 붙은 메서드로부터 **반환되는 객체**들을 스프링 컨테이너에 등록한다
  * 이 객체들이 **스프링 빈**이다.
  * Bean의 이름은 메서드명, value는 메서드의 반환값이다.
    * Bean 이름이 겹치면 안된다

```
@Bean
public MemberService memberService() {
	return new MemberServiceImpl(memberRepository());
}
```

	* 위와 같이 객체를 생성하는 메서드를 **팩토리 메서드**라고 한다

### ApplicationContext

> 스프링 컨테이너로서 등록된 Bean들을 관리한다

* `applicationContext.getBean("메서드명", 반환타입)` 을 통해 등록된 Bean을 꺼낸다

* `ApplicationContext`는 **인터페이스**이고,
* 구현체로 `AnnotationConfigApplicationContext`, `GenericXmlApplicationContext` 등이 다양하게 존재한다.

[image:0CFE449B-8DB2-4D5C-B8F7-4CBC112DA1FE-23864-00000A3AFEA4A9E4/3FA24F6B-C434-4653-A39F-21737F0623EF.png]

 
---

## BeanFactory 와 ApplicationContext

> **BeanFactory** 인터페이스에 부가 기능을 더한 것이 **ApplicationContext** 인터페이스

**BeanFactory** : 빈 조회하고 관리하는 걸 담당
**ApplicationContext** : 빈팩토리의 기능을 모두 상속 받아서 제공함. 거기에 +

* 국제화 기능 : 한글파일, 영어 파일 등 나눠줌
* 환경변수 : 개발 환경별 설정들을 처리해줌
* 애플리케이션 이벤트
* 편리한 리소스 조회

등등 애플리케이션 개발 시 필요한 기능들도 제공. 그래서 BeanFactory를 이용할 일은 없고 ApplicationContext만 사용한다고 보면된다 이를 **스프링 컨테이너**라고 한다

### 상속관계의 Bean 조회

> 부모타입으로 Bean 조회 시 자식타입이 전부 같이 조회된다

## xml 설정

* xml 기반의 설정은 요즘은 잘 안쓰이지만 여전히 쓰는데도 있으니 알아두자.
* appConfig.java와 비교해보면 구조가 완전 똑같다
* .java 파일이 아닌 파일은 전부 resources로 들어가면 된다

## Bean 메타 정보 (BeanDefinition)

> 건드릴 일은 거의 없지만 이런 것도 있다~ 정도만 알아두자

* **scope** : 할당안되어있으면 **싱글톤** Bean이라는 뜻
* **lazyInit** : default로 Bean들은 Bean컨테이너가 만들어질 때 등록이 된다. lazyInit으로 설정하면 Bean을 실제로 사용하는 시점에 등록을 하게된다

## 싱글톤 패턴

```
public class SingletonService {

	private final static SingletonService instance = new SingletonService();

	private SingletonService() {
	}

	public static SingletonService getInstance() {
		return instance;
	}
}
```

	* static 인스턴스 하나를 생성해놓고, 외부에서 `new`로 인스턴스를 새로 만들지 못하도록 **생성자를 private**으로 막는다
	* 인스턴스를 사용할 때는 `getInstance()` static 메서드를 호출하도록 한다

**문제점**

* 의존관계상 클라이언트가 구체 클래스에 의존한다(DIP 위반)
* 테스트하기 어렵다
* private 생성자로 자식 클래스를 만들기 어렵다 -> 결론적으로 유연성이 떨어진다

> 스프링은 객체들을 싱글톤으로 관리하면서 싱글톤 패턴의 단점들은 제거하였다.

* 싱글톤 객체를 생성하고, static 메서드인 `getInstance()`를 통해 인스턴스를 꺼내는 과정 때문에 위의 문제점들이 생긴다.
* Bean 컨테이너가 이러한 작업들을 다 처리해주기 때문에 애플리케이션 입장에서는 Bean 객체를 꺼내 쓰기만 하면 되므로 위의 문제점들이 해결된다.

### 싱글톤 방식의 주의점

**무상태로 설계해야한다**

* 특정 클라이언트에 의존적인 필드가 있으면 안된다
* 가급적이면 읽기만 가능해야하고, 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다
* 스프링 빈의 필드에 **공유 변수**를 두면 안된다

### CGLIBS

@Bean 이 붙은 메서드마다 이미 스프링 빈이 존재하면 그 빈을 반환하고, 존재하지 않으면 해당 메서드를 호출해서 빈을 새로 생성한다. 이렇게 싱글톤을 유지한다

### @Configuration 을 안붙이고 @Bean만 붙여둔다면?

CGLIBS가 적용되지 않아서 싱글톤이 깨진다 (ie:  memberRepository 가 여러번 호출 된다)

---

### @ComponentScan

* @Component가 붙은 클래스를 찾아서 자동으로 스프링빈으로 등록해준다
* 스캔의 default 범위는 @ComponentScan 이 붙은 클래스가 위치한 디렉토리 내의 모든 디렉토리이다.
  (ex: `com.hello`, `com.hello.controller`,`com.hello.repository` 같은 구조로 되어있다면, 이 설정파일은 `com.hello`에 둬야한다)
* @SpringBootApplication (Main 클래스에 붙어있음) 에 @Component 스캔이 포함되어 있기 때문에 사실 따로 붙여줄 필요가 없다

> 참고: @Service, @Controller 등만 붙여도 @Component로 인식이 된다.
> 이건 Annotation이 상속 되는게 아니라, 스프링에서 처리해주기 때문에 가능한 것이다.
> 자바 언어가 지원하는 기능이 아니다

### @Autowired

생성자에 붙일 경우, 생성자의 파라미터들을 빈 컨테이너에서 찾아서 자동으로 주입해준

### @Service

* 비즈니스 로직이 주로 담기는 곳. 하지만 스프링에서 특별한 처리를 하는것은 아니다

### 수동 빈과 자동 빈의 이름이 겹칠 경우

* 수동빈이 우선순위를 가진다.
* 하지만 최근 스프링은 `spring.main.allow-bean-definition-overriding=false` 가 default로 되어있어서 겹치는 빈이 있다면 아예 에러를 내버린다.

> 깔끔하지만 헷갈리는 로직 보다는, 중복이 있더라도 명확한 로직을 구현하자.

## 의존 관계 주입 방법

### 생성자 주입

> 생성자에 @Autowired를 달아주면, 파라미터의 객체들을 빈 컨테이너에서 찾아서 주입한다.
> 항상 생성자 주입을 먼저 사용하고, 필요한 경우에만 다른 방법을 적용하자

* 생성자가 하나일때는 @Autowired 를 생략하여도 된다.
* 빈을 등록하면서 의존성 주입도 바로 같이 일어난다
* 생성자 호출 시점에 딱 한 번만 호출되는 것이 보장된다(**불편**)
* Bean을 등록하려면 객체를 생성해야하기 때문에, 등록 단계에서 바로 의존성이 주입된다.

### 수정자(setter) 주입

* 외부에서 의존성을 건드릴 수 있기 때문에 추천하지 않는 방법
* 선택, 변경 가능성이 있는 의존관계에 사용

### 필드 주입

`@Autowired private final MemberRepository memberRepository`

* 외부에서 조작을 할 수 없어서 테스트가 힘들다. 차라리 setter 주입이 낫다.
* **테스트코드** 내에서와 같은 특수한 경우에만 쓰고, 애플리케이션 내에서는 쓰지말자

---

## @Autowired 옵션

#### required = false

```java
@Autowired(required = false)
public void setNoBean1(Member noBean1){
        System.out.println("noBean1 = "+noBean1);
        }
```

	* `required = false` 일때는 Bean이 없으면 메서드 자체가 실행되지 않는다 
	* default인 `required = true` 일때는 Bean 이 없으면 에러가 발생한다.

#### @Nullable

```java
@Autowired
public void setNoBean2(@Nullable Member noBean2){
        System.out.println("noBean2 = "+noBean2);
        }

```

	* @Nullable이 붙으면 Bean이 없어도 @Autowired(required=true)로 인한 에러가 발생하지 않고 null이 대신 들어간다.
	* 생성자 주입에서도 특정 파라미터는 꼭 필요하지 않을 때 @Nullable을 사용할 수 있다

---

### 조회 Bean 이 2개 이상일 때

@Autowired는 타입으로 빈을 조회한다. 같은 타입의 빈이 2개 이상 있을 경우, 예를 들어 **DiscountPolicy** 타입이 **FixedDiscountPolicy**, **
RateDiscountPolicy** 두 개가 모두 빈으로 등록되어 있을때

1. @Autowired **필드 또는 파라미터 명**을 선택할 빈의 이름으로 지정한다

> 생성자의 파라미터를 `DiscountPolicy rateDiscountPolicy` 형태로 지정하면 RateDiscountPolicy가 주입된다.

2. **@Qualifier** 을 사용한다

* Bean을 등록할 때 식별자를 달아놓는다.

```java
@Bean
@Qualifier("mainDiscountPolicy")
public DiscountPolicy discountPolicy(){..}
```

	* 빈을 사용할 때도 Qualifier를 달아줘야한다
	`@Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy`

3. **@Primary** 를 사용한다

> 빈 등록 시 @Primary를 붙여준다
 
---

### 커스텀 Annotation

```java

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {

}
```

### Map, List 로 여러 빈 조회

```java
private final Map<String, DiscountPolicy> policyMap;
private final List<DiscountPolicy> policies;

@Autowired
public DiscountService(Map<String, DiscountPolicy> policyMap,
        List<DiscountPolicy> policies){
        this.policyMap=policyMap;
        this.policies=policies;
        System.out.println("policyMap = "+policyMap);
        System.out.println("policies = "+policies);
        }
```

* 이렇게 하면 등록된 `Bean`들 중 **DiscountPolicy** 타입 빈들이 Map과 List에 담기게된다.
* 그리고 아래 메서드와 같이 다형성을 이용할 수 있게 된다.

```java
public int discount(Member member,int price,String discountCode){
        DiscountPolicy discountPolicy=policyMap.get(discountCode);
        return discountPolicy.discount(member,price);
        }
```

---

## 빈 생명주기 콜백

### 스프링 빈의 이벤트 라이프사이클

> 컨테이너 생성
> -> Bean 생성,등록(+생성자 의존관계 주입) -> 의존관계 주입(필드, setter)
> -> **초기화 콜백** -> 애플리케이션에서 빈 사용 -> **소멸 전 콜백**
> -> 스프링 종료

**생성과 초기화 단계를 분리하는게 좋다**

* **생성 단계**에서는 생성에 필수적인 (내부 값을 변경하는 등의 단순한, 가벼운) 작업을 하고,
* 외부와 커넥션을 맺는 등의 무거운 동작을 하는 코드는 **초기화 단계**에 두도록 한다.

### 초기화, 소멸 메서드 지정

**방법 1. InitializingBean, DisposableBean**
> 이 두 인터페이스를 구현하여 초기화, 소멸 메서드를 만들어줄 수 있지만 스프링 초기에 만들어진 방법이라 잘 쓰이지 않는다

**방법 2. @Bean(initMethod = “init”, destroyMethod = “close”)**
> 빈 등록 시점에 초기화와 소멸 메서드를 지정해줄 수 있다

**방법 3. ✅ @PostConstruct, @PreDestroy 사용**
> 이 방법을 사용하도록 한다. 해당 메서드에 어노테이션만 붙이면 끝난다.
> 다만 코드를 고칠 수 없는 외부 라이브러리에 대해서는 방법2를 쓰면 된다.

---

## 빈 스코프

### Singleton, Prototype

* **싱글톤** : 스프링 컨테이너의 시작시 빈 초기화 까지 전부 진행된 뒤, 컨테이너가 종료될 때 까지 유지된다. 가장 넓은 범위의 스코프이다.
* **프로토타입** : 스프링 컨테이너 시작시 **빈 생성**, **의존관계 주입** 까지 해준 뒤, 빈을 사용할 때 **초기화** 메서드를 호출한다. 그리고 그 뒤로는 컨테이너에서 일절 관리해주지 않는다

#### ObjectProvider

> **Dependency Lookup**(DL; 의존관계 탐색 기능) 제공. `ac.getBean()`와 같다.

딱 의존 관계 탐색( i.e. 빈 꺼내기)만 하면 될 때는 ApplicationContext를 통째로 불러올 필요 없이 **ObjectProvider**를 사용하면 된다. `provider.getObject()`

```java
private final ObjectProvider<PrototypeBean> prototypeBeanProvider;

// 생성자 + @Autowired

public int logic(){
        PrototypeBean bean=prototypeBeanProvider.getObject()
        ...
        }
```

**싱글톤으로 빈 안에서 프로토타입이 자동 주입된다면**?

* 싱글톤 빈의 의존성 주입이 처음 빈 생성 시 한번 밖에 안 일어나기 때문에 요청이 여러번 와도 처음에 만들었던 프로토타입 인스턴스를 계속 사용하게 된다. 즉, 프로토타입 빈도 싱글톤 빈처럼 사용된다.
* 이를 방지하려면 프로토타입 빈은 매번 새로 호출되도록 해줘야한다.

## 웹 스코프

### request

웹 요청이 들어오고 나갈 때 까지 유지된다. 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성된다.
`@Scope("reqeust")`

* Controller 단에서 request scope 빈을 자동주입하게 되면, 컨테이너가 생성되는 단계에서는 request가 아직 오지 않은 상태이기 때문에 에러가 발생한다. 이 경우 이전에 다룬 **
  ObjectProvider**를 이용해 꺼내면 된다.

* 더 편한 방법으로는, 의존 관계 주입시 가짜 객체인 **Proxy 객체**를 넣어두고, 빈을 꺼내서 객체의 메서드를 호출 할 때 실제 객체를 불러오는 방법이 있다.
  `@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS`


* 그 외 session, application, websocket scope 가 있다
