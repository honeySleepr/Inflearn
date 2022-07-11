package inflearn.springcorebasic.scan;

import inflearn.springcorebasic.AutoAppConfig;
import inflearn.springcorebasic.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class AutoAppConfigTest {

	@Test
	void basicScan() {
		ApplicationContext ac = new
			AnnotationConfigApplicationContext(AutoAppConfig.class);
		/* Main 클래스인 SpringCoreBasicApplication 에도 @ComponentScan이 포함되어 있어서
		위 Context에 AutoAppConfig 대신 넣어줘도 똑같이 작동한다*/
		MemberService memberService = ac.getBean(MemberService.class);
		assertThat(memberService).isInstanceOf(MemberService.class);
	}
}
