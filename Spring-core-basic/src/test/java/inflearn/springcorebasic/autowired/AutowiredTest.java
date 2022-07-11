package inflearn.springcorebasic.autowired;

import inflearn.springcorebasic.member.Member;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

public class AutowiredTest {

	@Test
	void AutowiredOption() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

	}

	static class TestBean {

		/* required = false 일때는 Bean이 없으면 메서드 자체가 실행되지 않는다 */
		/* 기본 값인 required = true 일때는 Bean 이 없으면 에러가 발생한다. */
		@Autowired(required = false)
		public void setNoBean1(Member noBean1) {
			System.out.println("noBean1 = " + noBean1);
		}

		/* @Nullable이 붙으면 Bean이 없어도 @Autowired(required=true)로 인한 에러가 발생하지 않고 null이 대신 들어간다. */
		@Autowired
		public void setNoBean2(@Nullable Member noBean2) {
			System.out.println("noBean2 = " + noBean2);
		}

		@Autowired
		public void setNoBean3(Optional<Member> noBean3) {
			System.out.println("noBean3 = " + noBean3);
		}
	}
}
