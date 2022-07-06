package inflearn.springcorebasic.order;

import inflearn.springcorebasic.member.Grade;
import inflearn.springcorebasic.member.Member;
import inflearn.springcorebasic.member.MemberService;
import inflearn.springcorebasic.member.MemberServiceImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest {

	MemberService memberService = new MemberServiceImpl();
	OrderService orderService = new OrderServiceImpl();

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
