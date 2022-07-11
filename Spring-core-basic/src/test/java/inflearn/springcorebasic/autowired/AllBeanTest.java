package inflearn.springcorebasic.autowired;

import inflearn.springcorebasic.AutoAppConfig;
import inflearn.springcorebasic.discount.DiscountPolicy;
import inflearn.springcorebasic.member.Grade;
import inflearn.springcorebasic.member.Member;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/* Map, List에 bean 담기, 여러 빈 조회 */
public class AllBeanTest {

	@Test
	void findAllBean() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
		DiscountService discountService = ac.getBean(DiscountService.class);
		Member member = new Member(1L, "memberA", Grade.VIP);
		int fixedDiscountPrice = discountService.discount(member, 20_000, "fixDiscountPolicy");
		assertThat(fixedDiscountPrice).isEqualTo(1_000);

		int rateDiscountPrice = discountService.discount(member, 20_000, "rateDiscountPolicy");
		assertThat(rateDiscountPrice).isEqualTo(2_000);
	}

	static class DiscountService {

		private final Map<String, DiscountPolicy> policyMap;
		private final List<DiscountPolicy> policies;

		@Autowired
		public DiscountService(Map<String, DiscountPolicy> policyMap,
			List<DiscountPolicy> policies) {
			this.policyMap = policyMap;
			this.policies = policies;
			System.out.println("policyMap = " + policyMap);
			System.out.println("policies = " + policies);
		}

		public int discount(Member member, int price, String discountCode) {
			DiscountPolicy discountPolicy = policyMap.get(discountCode);
			return discountPolicy.discount(member, price);
		}
	}

}
