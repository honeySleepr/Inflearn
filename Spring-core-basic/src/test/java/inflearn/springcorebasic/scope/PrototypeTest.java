package inflearn.springcorebasic.scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.assertThat;

public class PrototypeTest {

	@Test
	void prototypeBeanFind() {
		/* @Bean 또는 @ComponentScan을 안붙여주고 이렇게만 해도 빈으로 등록이 된다 */
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
		System.out.println("find Bean1");
		PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
		System.out.println("find Bean2");
		PrototypeBean bean2 = ac.getBean(PrototypeBean.class);
		System.out.println("bean1 = " + bean1);
		System.out.println("bean2 = " + bean2);
		assertThat(bean1).isNotSameAs(bean2);
		assertThat(bean1).isNotEqualTo(bean2);

		ac.close();
	}

	/* 프로토타입 빈은 싱글톤으로 관리되지 않고 매 요청시마다 새로운 인스턴스가 생성된다. */
	@Scope("prototype")
	static class PrototypeBean {

		@PostConstruct
		public void init() {
			System.out.println("PrototypeBean.init");
		}

		@PreDestroy
		public void destroy() {
			System.out.println("PrototypeBean.close");
		}

	}

}
