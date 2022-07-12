package inflearn.springcorebasic.scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonTest {

	@Test
	void singletonBeanFind() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);
		System.out.println("find Bean1");
		SingletonBean bean1 = ac.getBean(SingletonBean.class);
		System.out.println("find Bean2");
		SingletonBean bean2 = ac.getBean(SingletonBean.class);
		System.out.println("bean1 = " + bean1);
		System.out.println("bean2 = " + bean2);
		assertThat(bean1).isSameAs(bean2);
		assertThat(bean1).isEqualTo(bean2);

		ac.close();
	}

	@Scope("singleton")
	static class SingletonBean {

		@PostConstruct
		public void init() {
			System.out.println("SingletonBean.init");
		}

		@PreDestroy
		public void destroy() {
			System.out.println("SingletonBean.close");
		}

	}

}
