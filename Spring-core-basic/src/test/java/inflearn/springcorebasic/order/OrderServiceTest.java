package inflearn.springcorebasic.order;

import inflearn.springcorebasic.AppConfig;
import inflearn.springcorebasic.member.Grade;
import inflearn.springcorebasic.member.Member;
import inflearn.springcorebasic.member.MemberService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest {

	private final MemberService memberService;
	private final OrderService orderService;

	public OrderServiceTest() {
		AppConfig appConfig = new AppConfig();
		this.memberService = appConfig.memberService();
		this.orderService = appConfig.orderService();
	}

	@Test
	void createOrder() {

		// given
		Long memberId = 1L;
		Member member = new Member(memberId, "memberA", Grade.VIP);
		memberService.join(member);

		// when
		Order order = orderService.createOrder(memberId, "itemA", 10000);

		// then
		assertAll(
			() -> assertThat(order.calculatePrice()).isEqualTo(9000),
			() -> assertThat(order.getDiscountPrice()).isEqualTo(1000)
		);
	}
}
