package inflearn.springcorebasic.scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {

	@Test
	void prototypeFind() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

		PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
		bean1.addCount();
		assertThat(bean1.getCount()).isEqualTo(1);

		PrototypeBean bean2 = ac.getBean(PrototypeBean.class);
		bean2.addCount();
		assertThat(bean2.getCount()).isEqualTo(1);
	}

	@Test
	void singletonClientUsePrototype() {
		/* 코드는 생략한다..*/
		/* 요점은, 생성 시 매번 객체가 되는 프로토타입 빈이라고 해도,
		 * 싱글톤 빈 안에 주입되어 사용된다면 요청이 어려번 들어와도 의존성 주입이 한번 밖에 일어나지 않기 때문에
		 * 프로토타입 빈도 똑같이 한 인스턴스만 사용된다 */
	}

	@Scope("singleton")
	static class ClientBean {

		private final PrototypeBean prototypeBean; /* 생성 시점에 한 번 주입되고 끝 */

		@Autowired
		public ClientBean(PrototypeBean prototypeBean) {
			this.prototypeBean = prototypeBean;
		}
	}

	@Scope("prototype")
	static class PrototypeBean {

		private int count = 0;

		public void addCount() {
			count++;
		}

		public int getCount() {
			return this.count;
		}

		@PostConstruct
		public void init() {
			System.out.println("PrototypeBen.init " + this);
		}

		@PreDestroy
		public void destroy() {
			System.out.println("PrototypeBean.destroy");
		}
	}
}
